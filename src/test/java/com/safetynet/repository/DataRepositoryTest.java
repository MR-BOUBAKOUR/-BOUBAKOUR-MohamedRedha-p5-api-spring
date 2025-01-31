package com.safetynet.repository;

import com.safetynet.model.Firestation;
import com.safetynet.model.MedicalRecord;
import com.safetynet.model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DataRepositoryTest {

    List<Person> persons;
    List<Firestation> firestations;
    List<MedicalRecord> medicalRecords;

    private DataRepository dataRepository;

    @BeforeEach
    void setUp() throws Exception {
        Path testFile = Path.of("src/test/resources/data-test.json");
        Path tempFile = Path.of("src/test/resources/data-test-temp.json");

        Files.copy(testFile, tempFile, StandardCopyOption.REPLACE_EXISTING);

        dataRepository = new DataRepository();
        DataRepository.FILE_PATH = tempFile.toString();

        dataRepository.init();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void init_ShouldLoadDataCorrectlyTest() {

        persons = dataRepository.getPersons();
        firestations = dataRepository.getFirestations();
        medicalRecords = dataRepository.getMedicalRecords();

        assertNotNull(persons, "persons is not null");
        assertNotNull(firestations, "firestations is not null");
        assertNotNull(medicalRecords, "medicalRecords is not null");
    }

    @Test
    void init_ShouldThrowException_WhenFileNotFoundTest() {

        DataRepository invalidRepository = new DataRepository();
        DataRepository.FILE_PATH = "src/test/resources/missing-file.json";

        Exception exception = assertThrows(RuntimeException.class, invalidRepository::init);
        assertTrue(exception.getMessage().contains("Failed to load JSON file"));
    }

    @Test
    void readData_ShouldReadDataCorrectlyTest() {

        List<Person> result = dataRepository.readData("persons", Person.class);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(23, result.size());
    }

    @Test
    void writeData_ShouldWriteDataCorrectlyTest() {

        Person newPerson = new Person(
                "testFirstName",
                "testLastName",
                "testAddress",
                "testCity",
                99999,
                "999-999-9999",
                "testEmail@email.com"
        );
        dataRepository.getPersons().add(newPerson);
        List<Person> updatedPersons = dataRepository.getPersons();

        dataRepository.writeData("persons", updatedPersons);

        assertTrue(dataRepository.getPersons().contains(newPerson));
        assertEquals(24, dataRepository.getPersons().size());
    }

}