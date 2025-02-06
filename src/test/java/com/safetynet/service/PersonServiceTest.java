package com.safetynet.service;

import com.safetynet.dto.person.PersonCreateDTO;
import com.safetynet.dto.person.PersonResponseDTO;
import com.safetynet.dto.person.PersonUpdateDTO;
import com.safetynet.exception.ConflictException;
import com.safetynet.exception.ResourceNotFoundException;
import com.safetynet.mapper.PersonMapper;
import com.safetynet.model.Person;
import com.safetynet.repository.DataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PersonServiceTest {

    @Mock
    private DataRepository dataRepository;

    @Mock
    private PersonMapper personMapper;

    @InjectMocks
    private PersonService personService;

    @BeforeEach
    void setUp() {
        List<Person> persons = new ArrayList<>();
        persons.add(new Person("John", "Doe", "123 Main St", "Springfield", 12345, "555-555-5555", "john.doe@email.com"));
        persons.add(new Person("Jane", "Doe", "456 Elm St", "Shelbyville", 67890, "555-555-5556", "jane.doe@email.com"));

        when(dataRepository.getPersons()).thenReturn(persons);
        personService = new PersonService(dataRepository, personMapper);
    }

    @Test
    void findAllPersons_shouldReturnAllPersons() {
        when(personMapper.toResponseDTO(any(Person.class)))
                .thenReturn(new PersonResponseDTO("John", "Doe", "123 Main St", "Springfield", "12345"));

        List<PersonResponseDTO> result = personService.findAllPersons();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(dataRepository, times(2)).getPersons();
    }

    @Test
    void findPersonByFirstNameAndLastName_shouldReturnPersonResponseDTO() {
        when(personMapper.toResponseDTO(any(Person.class)))
                .thenReturn(new PersonResponseDTO("John", "Doe", "123 Main St", "Springfield", "12345"));

        PersonResponseDTO result = personService.findPersonByFirstNameAndLastName("John", "Doe");

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("123 Main St", result.getAddress());
        assertEquals("Springfield", result.getCity());
        assertEquals("12345", result.getZip());
        verify(dataRepository, times(2)).getPersons();
    }

    @Test
    void findPersonByFirstNameAndLastName_whenNotFound_shouldThrowException() {
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                personService.findPersonByFirstNameAndLastName("Nonexistent", "Person")
        );

        assertEquals("Resource not found", exception.getMessage());
    }

    @Test
    void addPerson_shouldAddNewPerson() {
        PersonCreateDTO personCreateDTO = new PersonCreateDTO("Alice", "Smith", "789 Oak St", "Metropolis", "11223", "555-555-5557", "alice.smith@email.com");

        when(personMapper.toEntityFromCreateDTO(personCreateDTO))
                .thenReturn(new Person("Alice", "Smith", "789 Oak St", "Metropolis", 11223, "555-555-5557", "alice.smith@email.com"));

        personService.addPerson(personCreateDTO);

        verify(dataRepository, times(1)).writeData(eq("persons"), anyList());
    }

    @Test
    void addPerson_whenPersonExists_shouldThrowConflictException() {

        PersonCreateDTO personCreateDTO = new PersonCreateDTO("John", "Doe", "123 Main St", "Springfield", "12345", "555-555-5555", "john.doe@email.com");

        ConflictException exception = assertThrows(ConflictException.class, () -> personService.addPerson(personCreateDTO));

        assertEquals("Resource already exist", exception.getMessage());
    }

    @Test
    void updatePerson_shouldUpdateExistingPerson() {

        PersonUpdateDTO personUpdateDTO = new PersonUpdateDTO("999 Pine St", "Gotham", "33445", "555-555-5558", "new.email@email.com");

        when(personMapper.toEntityFromUpdateDTO(personUpdateDTO))
                .thenReturn(new Person("John", "Doe", "999 Pine St", "Gotham", 33445, "555-555-5558", "new.email@email.com"));

        personService.updatePerson(personUpdateDTO, "John", "Doe");

        verify(dataRepository, times(1)).writeData(eq("persons"), anyList());
    }

    @Test
    void updatePerson_whenNotFound_shouldThrowException() {

        PersonUpdateDTO updateDTO = new PersonUpdateDTO("999 Pine St", "Gotham", "33445", "555-555-5558", "new.email@email.com");

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                personService.updatePerson(updateDTO, "Nonexistent", "Person")
        );

        assertEquals("Resource not found", exception.getMessage());
    }

    @Test
    void deletePerson_shouldRemovePerson() {

        personService.deletePerson("John", "Doe");

        verify(dataRepository, times(1)).writeData(eq("persons"), anyList());
    }

    @Test
    void deletePerson_whenNotFound_shouldThrowException() {

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                personService.deletePerson("Nonexistent", "Person")
        );

        assertEquals("Resource not found", exception.getMessage());
    }
}
