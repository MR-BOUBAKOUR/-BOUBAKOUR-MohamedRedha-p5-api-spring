package com.safetynet.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.safetynet.model.Firestation;
import com.safetynet.model.MedicalRecord;
import com.safetynet.model.Person;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class DataAccessService {

    private static final Logger logger = LoggerFactory.getLogger(DataAccessService.class);

    private static final String FILE_PATH = "src/main/resources/data.json";

    private final ObjectMapper mapper = new ObjectMapper();
    private JsonNode rootNode;

    // Load the JSON file only once at the start of the application.
    // This ensures that the data is available in memory for all subsequent calls.
    @PostConstruct
    public void init() {
        try {
            rootNode = mapper.readTree(new File(FILE_PATH));
            logger.info("JSON file loaded successfully.");
        } catch (IOException e) {
            logger.error("Error reading the JSON file", e);
            throw new RuntimeException("Failed to load JSON file", e);
        }
    }

    public List<Person> findPersons() {
        return getDataList("persons", Person.class);
    }

    public Person findPersonByFirstNameAndLastName(String firstName, String lastName) {
        List<Person> Persons = findPersons();
        for (Person person : Persons) {
            if (person.getFirstName().equals(firstName) && person.getLastName().equals(lastName)) {
                return person;
            }
        }
        return null;
    }

    public List<Firestation> findFirestations() {
        return getDataList("firestations", Firestation.class);
    }

    public List<MedicalRecord> findMedicalRecords() {
        return getDataList("medicalrecords", MedicalRecord.class);
    }

    private <T> List<T> getDataList(String theNodeName, Class<T> theClass) {
        try {
            JsonNode node = rootNode.path(theNodeName);

            CollectionType listType = mapper.getTypeFactory()
                    .constructCollectionType(List.class, theClass);

            return mapper.readValue(node.traverse(), listType);
        } catch (IOException e) {
            logger.error("Error deserializing JSON node: " + theNodeName, e);
            return Collections.emptyList();
        }
    }

    public void reloadData() {
        init();
    }
}

/*
@Service
public class DataAccessService {

    private static final Logger logger = LoggerFactory.getLogger(DataAccessService.class);

    private static final String FILE_PATH = "src/main/resources/data.json";

    private ObjectMapper mapper = new ObjectMapper();

    public List<Person> findPersons() {
        try {
            // Read the JSON file as a JsonNode
            JsonNode rootNode = mapper.readTree(new File(FILE_PATH));

            // Extract the "persons" array
            JsonNode personsNode = rootNode.path("persons");

            // Define the collection type for deserialization
            CollectionType listType = mapper.getTypeFactory()
                    .constructCollectionType(List.class, Person.class);

            // Deserialize the "persons" array into a List<Person>
            return mapper.readValue(personsNode.traverse(), listType);

        } catch (IOException e) {
            logger.error("Error reading the JSON file", e);
            return null;
        }
    }

    public List<Firestation> findFirestations() {
        try {
            JsonNode rootNode = mapper.readTree(new File(FILE_PATH));
            JsonNode firestationsNode = rootNode.path("firestations");

            CollectionType listType = mapper.getTypeFactory()
                    .constructCollectionType(List.class, Firestation.class);

            return mapper.readValue(firestationsNode.traverse(), listType);
        } catch (IOException e) {
            logger.error("Error reading the JSON file", e);
            return null;
        }
    }

    public List<MedicalRecord> findMedicalRecords() {
        try {
            JsonNode rootNode = mapper.readTree(new File(FILE_PATH));
            JsonNode medicalRecordsNode = rootNode.path("medicalrecords");

            CollectionType listType = mapper.getTypeFactory()
                    .constructCollectionType(List.class, MedicalRecord.class);

            return mapper.readValue(medicalRecordsNode.traverse(), listType);
        } catch (IOException e) {
            logger.error("Error reading the JSON file", e);
            return null;
        }
    }
}
*/