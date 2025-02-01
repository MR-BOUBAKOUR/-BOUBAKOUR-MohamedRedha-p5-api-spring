package com.safetynet.repository;

import com.safetynet.model.Firestation;
import com.safetynet.model.MedicalRecord;
import com.safetynet.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DataRepositoryTest {

    private static DataRepository dataRepository;

    @BeforeEach
    void setUp() throws Exception {
        Path testFile = Path.of("src/test/resources/data-test.json");
        Path tempFile = Path.of("src/test/resources/data-test-temp.json");

        Files.copy(testFile, tempFile, StandardCopyOption.REPLACE_EXISTING);

        dataRepository = new DataRepository();
        DataRepository.FILE_PATH = tempFile.toString();

        dataRepository.init();
    }

    @Test
    void init_ShouldLoadDataCorrectlyTest() {

        List<Person> persons = dataRepository.getPersons();
        List<Firestation> firestations = dataRepository.getFirestations();
        List<MedicalRecord> medicalRecords = dataRepository.getMedicalRecords();

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
    void readData_ShouldReturnEmptyList_WhenNodeDoesNotExist() {
        String nonExistentNode = "nonExistentNode";

        List<?> result = dataRepository.readData(nonExistentNode, Object.class);

        assertTrue(result.isEmpty());
    }

    @Test
    void readData_ShouldReadPersonsDataCorrectlyTest() {

        List<Person> persons = dataRepository.readData("persons", Person.class);

        assertEquals(23, persons.size());

        assertNotNull(persons);
        for (Person person : persons) {
            assertNotNull(person.getFirstName());
            assertNotNull(person.getLastName());
            assertNotNull(person.getAddress());
            assertNotNull(person.getCity());
            assertTrue(person.getZip() > 0);
            assertNotNull(person.getPhone());
            assertNotNull(person.getEmail());
        }
    }

    @Test
    void writeData_ShouldWriteDataCorrectlyTest() {

        List<Person> persons = dataRepository.getPersons();
        int initPersonsCount = persons.size();

        Person newPerson = new Person(
            "testFirstName",
            "testLastName",
            "testAddress",
            "testCity",
            99999,
            "999-999-9999",
            "testEmail@email.com"
        );
        persons.add(newPerson);

        dataRepository.writeData("persons", persons);

        assertTrue(persons.contains(newPerson));
        assertEquals(initPersonsCount + 1, dataRepository.getPersons().size());
    }

}