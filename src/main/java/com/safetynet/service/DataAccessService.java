package com.safetynet.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

    private List<Person> personsCache;
    private List<Firestation> firestationsCache;
    private List<MedicalRecord> medicalRecordsCache;

    // Load the JSON file only once at the start of the application.
    // This ensures that the data is available in memory for all subsequent calls.
    @PostConstruct
    public void init() {
        try {
            rootNode = mapper.readTree(new File(FILE_PATH));

            // The loaded data is sent to its respective "cache" variable (for performance issues)
            personsCache = readData("persons", Person.class);
            firestationsCache = readData("firestations", Firestation.class);
            medicalRecordsCache = readData("medicalrecords", MedicalRecord.class);

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

    private <T> void writeData(String theNodeName, List<T> data) {
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

    public void reloadData() {
        init();
    }

    // -------------------------- Persons --------------------------

    public List<Person> findAllPersons() {
        logger.info("Finding the persons from the database");
        return personsCache;
    }

    public Person findPersonByFirstNameAndLastName(String theFirstName, String theLastName) {
        for (Person person : personsCache) {
            if (person.getFirstName().equals(theFirstName) && person.getLastName().equals(theLastName)) {
                logger.info("Found the person with the first name {} and last name {}", person.getFirstName(), person.getLastName());
                return person;
            }
        }
        return null;
    }

    public void addPerson(Person person) {
        personsCache.add(person);
        writeData("persons", personsCache);
        logger.info("{} added successfully", person.getFirstName());
    }

    public void updatePerson(Person thePerson) {
        boolean found = false;
        for (Person person : personsCache) {
            if (person.getFirstName().equals(thePerson.getFirstName()) &&
                    person.getLastName().equals(thePerson.getLastName())) {

                // Replace the existing person with the updated person
                int index = personsCache.indexOf(person);
                personsCache.set(index, thePerson);

                writeData("persons", personsCache);
                logger.info("{} updated successfully", person.getFirstName());
                found = true;
            }
        }
        if (!found) logger.error("{} not found", thePerson.getFirstName());
    }

    public void deletePerson(String theFirstName, String theLastName) {
        // Not using the "persons.remove(person)" to avoid ConcurrentModificationException.
        // Occurs when a collection is modified while it is being iterated over.
        personsCache.removeIf(person -> person.getFirstName().equals(theFirstName) && person.getLastName().equals(theLastName));
        writeData("persons", personsCache);
        logger.info("{} deleted successfully", theFirstName);
    }

    // -------------------------- Firestations --------------------------

    public List<Firestation> findAllFirestations() {
        logger.info("Finding the firestations from the database");
        return firestationsCache;
    }

    public Firestation findFirestationByAddress(String theAddress) {
        for (Firestation firestation : firestationsCache) {
            if (firestation.getAddress().equals(theAddress)) {
                logger.info("Found the firestation with the address {}", firestation.getAddress());
                return firestation;
            }
        }
        return null;
    }

    public void addFirestation(Firestation theFirestation) {
        firestationsCache.add(theFirestation);
        writeData("firestations", firestationsCache);
        logger.info("{} added successfully", theFirestation.getAddress());
    }

    public void updateFirestation(Firestation theFirestation) {
        boolean found = false;
        for (Firestation firestation : firestationsCache) {
            if (firestation.getAddress().equals(theFirestation.getAddress())) {
                int index = firestationsCache.indexOf(firestation);
                firestationsCache.set(index, theFirestation);
                writeData("firestations", firestationsCache);
                logger.info("{} updated successfully", theFirestation.getAddress());
                found = true;
            }
        }
        if (!found) logger.error("{} not found", theFirestation.getAddress());
    }

    public void deleteFirestation(String theAddress) {
        firestationsCache.removeIf(firestation -> firestation.getAddress().equals(theAddress));
        writeData("firestations", firestationsCache);
        logger.info("{} deleted successfully", theAddress);
    }

    // -------------------------- MedicalRecord --------------------------

    public List<MedicalRecord> findAllMedicalRecords() {
        logger.info("Finding the medical records from the database");
        return medicalRecordsCache;
    }

    public MedicalRecord findMedicalrecordByFirstNameAndLastName(String theFirstName, String theLastName) {
        for (MedicalRecord medicalRecord : medicalRecordsCache) {
            if (medicalRecord.getFirstName().equals(theFirstName) && medicalRecord.getLastName().equals(theLastName)) {
                logger.info("Found the medicalRecord with the first name {} and last name {}", medicalRecord.getFirstName(), medicalRecord.getLastName());
                return medicalRecord;
            }
        }
        return null;
    }

    public void addMedicalrecord(MedicalRecord theMedicalrecord) {
        medicalRecordsCache.add(theMedicalrecord);
        writeData("medicalrecords", medicalRecordsCache);
        logger.info("{} added successfully", theMedicalrecord.getFirstName());
    }

    public void updateMedicalrecord(MedicalRecord theMedicalrecord) {
        boolean found = false;
        for (MedicalRecord medicalRecord : medicalRecordsCache) {
            if (medicalRecord.getFirstName().equals(theMedicalrecord.getFirstName()) &&
                    medicalRecord.getLastName().equals(theMedicalrecord.getLastName())) {
                int index = medicalRecordsCache.indexOf(medicalRecord);
                medicalRecordsCache.set(index, theMedicalrecord);
                writeData("medicalRecords", medicalRecordsCache);
                logger.info("{} updated successfully", theMedicalrecord.getFirstName());
                found = true;
            }
        }
        if (!found) logger.error("{} not found", theMedicalrecord.getFirstName());
    }

    public void deleteMedicalrecord(String theFirstName, String theLastName) {
        medicalRecordsCache.removeIf(medicalRecord -> medicalRecord.getFirstName().equals(theFirstName) && medicalRecord.getLastName().equals(theLastName));
        writeData("medicalRecord", medicalRecordsCache);
        logger.info("{} deleted successfully", theFirstName);
    }
}

/*
@Service
public class DataAccessService {

    private static final Logger logger = LoggerFactory.getLogger(DataAccessService.class);

    private static final String FILE_PATH = "src/main/resources/data.json";

    private ObjectMapper mapper = new ObjectMapper();

    public List<Person> findAllPersons() {
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

    public List<Firestation> findAllFirestations() {
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

    public List<MedicalRecord> findAllMedicalRecords() {
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