package com.safetynet.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.safetynet.model.Firestation;
import com.safetynet.model.MedicalRecord;
import com.safetynet.model.Person;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class DataRepository {

    private static final Logger logger = LoggerFactory.getLogger(DataRepository.class);

    private static final String FILE_PATH = "src/main/resources/data.json";
    private final ObjectMapper mapper = new ObjectMapper();
    private JsonNode rootNode;

    // Accessible from their respective getters
    @Getter
    private List<Person> persons;
    @Getter
    private List<Firestation> firestations;
    @Getter
    private List<MedicalRecord> medicalRecords;

    // Load the JSON file only once at the start of the application.
    // This ensures that the data is available in memory for all subsequent calls.
    @PostConstruct
    public void init() {
        try {
            rootNode = mapper.readTree(new File(FILE_PATH));

            // The loaded data is sent to its respective "cache" variable (for performance issues)
            persons = readData("persons", Person.class);
            firestations = readData("firestations", Firestation.class);
            medicalRecords = readData("medicalrecords", MedicalRecord.class);

            logger.info("JSON file loaded successfully.");
        } catch (IOException e) {
            logger.error("Error reading the JSON file", e);
            throw new RuntimeException("Failed to load JSON file", e);
        }
    }

    private <T> List<T> readData(String theNodeName, Class<T> theClass) {
        try {
            // Retrieve the specified node (e.g., "persons", "firestations", etc.) from the root object using the provided node name.
            JsonNode node = rootNode.path(theNodeName);

            // Create a collection type representing a "List" of the specified class type "theClass".
            // (This tells Jackson's ObjectMapper HOW to deserialize the JSON data)
            CollectionType listType = mapper.getTypeFactory()
                    .constructCollectionType(List.class, theClass);

            // Deserialize the JSON node into a List of the specified type using ObjectMapper.
            // In our case : Jackson's ObjectMapper go from the "node" in the JSON file to a "List<theClass>"
            logger.info("Deserializing the node -{}-", theNodeName);
            return mapper.readValue(node.traverse(), listType);
        } catch (IOException e) {
            logger.error("Error deserializing JSON node: -{}-", theNodeName, e);
            return Collections.emptyList();
        }
    }

    public <T> void writeData(String theNodeName, List<T> data) {
        try {
            // Create a deep copy of the rootNode to preserve the original structure.
            JsonNode actualRootNode = rootNode.deepCopy();

            // Set the specified node (e.g., "persons", "firestations", etc.) with the new data.
            // `valueToTree` converts the List<T> into a JSON node representation.
            ObjectNode updatedRootNode = (ObjectNode) actualRootNode;
            updatedRootNode.set(theNodeName, mapper.valueToTree(data));

            // Write the updated rootNode back to the file.
            // This ensures that the changes are persisted in the JSON file.
            // The updatedRootNode will go back to the Json format by using the mapper
            mapper.writeValue(new File(FILE_PATH), updatedRootNode);
            logger.info("-{}- data written successfully", theNodeName);
        } catch (IOException e) {
            logger.error("Error writing data to JSON file", e);
        }
    }

}