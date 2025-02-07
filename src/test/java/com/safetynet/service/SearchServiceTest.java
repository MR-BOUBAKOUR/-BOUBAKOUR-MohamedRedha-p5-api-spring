package com.safetynet.service;

import com.safetynet.dto.search.*;
import com.safetynet.exception.ResourceNotFoundException;
import com.safetynet.mapper.PersonMapper;
import com.safetynet.model.Firestation;
import com.safetynet.model.MedicalRecord;
import com.safetynet.model.Person;
import com.safetynet.repository.DataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class SearchServiceTest {

    @Mock
    private DataRepository dataRepository;

    @Mock
    private PersonMapper personMapper;

    @InjectMocks
    private SearchService searchService;

    @BeforeEach
    void setUp() {
        List<Person> persons = List.of(
                new Person("John", "Doe", "123 Main St", "Springfield", 12345, "555-555-5555", "john.doe@email.com"),
                new Person("Jane", "Doe", "123 Main St", "Springfield", 12345, "555-555-5556", "jane.doe@email.com")
        );
        List<Firestation> firestations = List.of(new Firestation("123 Main St", 1));
        List<MedicalRecord> medicalRecords = List.of(
                new MedicalRecord("John", "Doe", "01/01/2000", List.of("Med1"), List.of("Allergy1")),
                new MedicalRecord("Jane", "Doe", "01/01/2018", List.of("Med2"), List.of("Allergy2"))
        );
        when(dataRepository.getPersons()).thenReturn(persons);
        when(dataRepository.getFirestations()).thenReturn(firestations);
        when(dataRepository.getMedicalRecords()).thenReturn(medicalRecords);
        searchService = new SearchService(dataRepository, personMapper);
    }

    @Test
    void getCoveredPersonsByStation_shouldReturnCoveredPersons() {
        FirestationCoverageResponseDTO response = searchService.getCoveredPersonsByStation(1);

        assertNotNull(response);
        assertEquals(1, response.getAdultCount().get());
        assertEquals(1, response.getChildCount().get());
    }

    @Test
    void getCoveredPersonsByStation_whenStationNotFound_shouldThrowException() {
        assertThrows(ResourceNotFoundException.class, () -> searchService.getCoveredPersonsByStation(99));
    }

    @Test
    void getChildrenByAddress_shouldReturnChildren() {
        ChildAlertResponseDTO response = searchService.getChildrenByAddress("123 Main St");
        assertNotNull(response);
        assertEquals(1, response.getChildren().size());
    }

    @Test
    void getChildrenByAddress_whenAddressNotFound_shouldReturnEmpty() {
        ChildAlertResponseDTO response = searchService.getChildrenByAddress("Unknown Address");
        assertNotNull(response);
        assertTrue(response.getChildren().isEmpty());
    }

    @Test
    void getPhonesByStation_shouldReturnPhones() {
        PhoneAlertResponseDTO response = searchService.getPhonesByStation(1);
        assertNotNull(response);
        assertEquals(2, response.getPhones().size());
    }

    @Test
    void getPhonesByStation_whenStationNotFound_shouldThrowException() {
        assertThrows(ResourceNotFoundException.class, () -> searchService.getPhonesByStation(99));
    }

    @Test
    void getPersonsByAddressStation_shouldReturnPersonsAndStations() {
        FireResponseDTO response = searchService.getPersonsByAddressStation("123 Main St");
        assertNotNull(response);
        assertFalse(response.getPersons().isEmpty());
        assertEquals(1, response.getStations().getFirst());
        assertEquals(2, response.getPersons().size());
    }

    @Test
    void getPersonsByAddressStation_whenAddressNotFound_shouldThrowException() {
        assertThrows(ResourceNotFoundException.class, () -> searchService.getPersonsByAddressStation("Unknown Address"));
    }

    @Test
    void getPersonsByStationsWithMedicalRecord_shouldReturnPersons() {
        FloodStationsResponseDTO response = searchService.getPersonsByStationsWithMedicalRecord(List.of(1));
        assertNotNull(response);
        assertEquals(2, response.getPersons().size());
    }

    @Test
    void getPersonsByStationsWithMedicalRecord_whenStationNotFound_shouldThrowException() {
        assertThrows(ResourceNotFoundException.class, () -> searchService.getPersonsByStationsWithMedicalRecord(List.of(8, 9)));
    }

    @Test
    void getPersonByLastNameWithMedicalRecord_shouldReturnPersons() {
        PersonsInfoLastNameResponseDTO response = searchService.getPersonByLastNameWithMedicalRecord("Doe");
        assertNotNull(response);
        assertEquals(2, response.getPersons().size());
    }

    @Test
    void getPersonByLastNameWithMedicalRecord_whenLastNameNotFound_shouldThrowException() {
        assertThrows(ResourceNotFoundException.class, () -> searchService.getPersonByLastNameWithMedicalRecord("Unknown Last Name"));
    }

    @Test
    void getEmailsByCity_shouldReturnEmails() {
        CommunityEmailResponseDTO response = searchService.getEmailsByCity("Springfield");
        assertNotNull(response);
        assertEquals("john.doe@email.com", response.getEmails().getFirst());
    }

    @Test
    void getEmailsByCity_whenCityNotFound_shouldThrowException() {
        assertThrows(ResourceNotFoundException.class, () -> searchService.getEmailsByCity("Unknown City"));
    }
}
