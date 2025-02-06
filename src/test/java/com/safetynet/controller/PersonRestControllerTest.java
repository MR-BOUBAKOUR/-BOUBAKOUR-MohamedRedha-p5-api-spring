package com.safetynet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.dto.person.PersonCreateDTO;
import com.safetynet.dto.person.PersonResponseDTO;
import com.safetynet.dto.person.PersonUpdateDTO;
import com.safetynet.exception.ConflictException;
import com.safetynet.exception.ResourceNotFoundException;
import com.safetynet.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(PersonRestController.class)
class PersonRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PersonService personService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllPersons_Success_test() throws Exception {
        List<PersonResponseDTO> persons = List.of(
            new PersonResponseDTO("John", "Doe", "123 Street", "City1", "12345"),
            new PersonResponseDTO("Jane", "Smith", "456 Avenue", "City2", "67890")
        );

        when(personService.findAllPersons())
            .thenReturn(persons);

        mockMvc.perform(get("/person"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(persons.size()))
            .andExpect(jsonPath("$[0].firstName").value("John"))
            .andExpect(jsonPath("$[0].lastName").value("Doe"))
            .andExpect(jsonPath("$[1].firstName").value("Jane"))
            .andExpect(jsonPath("$[1].lastName").value("Smith"));
    }

    @Test
    void getPersonByFirstNameAndLastName_Success_test() throws Exception {
        String firstName = "John";
        String lastName = "Doe";
        PersonResponseDTO person = new PersonResponseDTO(
            firstName,
            lastName,
            "123 Street",
            "City1",
            "12345"
        );

        when(personService.findPersonByFirstNameAndLastName(firstName, lastName))
            .thenReturn(person);

        mockMvc.perform(get("/person/{theFirstName}-{theLastName}", firstName, lastName))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value(firstName))
            .andExpect(jsonPath("$.lastName").value(lastName));
    }

    @Test
    void getPersonByFirstNameAndLastName_ResourceNotFound_test() throws Exception {
        String firstName = "Unknown";
        String lastName = "Person";
        String errorResponse = "Resource not found";

        when(personService.findPersonByFirstNameAndLastName(firstName, lastName))
            .thenThrow(new ResourceNotFoundException(errorResponse));

        mockMvc.perform(get("/person/{theFirstName}-{theLastName}", firstName, lastName))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.message").value(errorResponse));
    }

    @Test
    void addPerson_Success_test() throws Exception {
        PersonCreateDTO newPerson = new PersonCreateDTO(
            "John",
            "Doe",
            "123 Street",
            "City1",
            "12345",
            "123-456-7890",
            "john.doe@example.com"
        );
        String successResponse = "Person added successfully";

        doNothing()
            .when(personService).addPerson(any(PersonCreateDTO.class));

        mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newPerson)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.status").value(201))
            .andExpect(jsonPath("$.message").value(successResponse));
    }

    @Test
    void addPerson_Conflict_test() throws Exception {
        PersonCreateDTO existingPerson = new PersonCreateDTO(
            "John",
            "Boyd",
            "789 Boulevard",
            "City2",
            "67890",
            "987-654-3210",
            "john.boyd@example.com"
        );
        String errorResponse = "Resource already exists";

        doThrow(new ConflictException(errorResponse))
            .when(personService).addPerson(any(PersonCreateDTO.class));

        mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(existingPerson)))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.status").value(409))
            .andExpect(jsonPath("$.message").value(errorResponse));
    }

    @Test
    void updatePerson_Success_test() throws Exception {
        String firstName = "John";
        String lastName = "Doe";
        PersonUpdateDTO updatedPerson = new PersonUpdateDTO(
            "1234 New Street",
            "NewCity",
            "54321",
            "321-654-9870",
            "john.doe@newemail.com"
        );
        String successResponse = "Person updated successfully";

        doNothing()
            .when(personService).updatePerson(
                    any(PersonUpdateDTO.class),
                    eq(firstName),
                    eq(lastName)
            );

        mockMvc.perform(put("/person/{theFirstName}-{theLastName}", firstName, lastName)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedPerson)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(200))
            .andExpect(jsonPath("$.message").value(successResponse));
    }

    @Test
    void updatePerson_Conflict_test() throws Exception {
        String firstName = "John";
        String lastName = "Boyd";
        PersonUpdateDTO updatedPerson = new PersonUpdateDTO(
            "789 Boulevard",
            "City2",
            "67890",
            "987-654-3210",
            "john.boyd@example.com"
        );
        String errorResponse = "The person with this name is exactly the same as the one you are trying to update";

        doThrow(new ConflictException(errorResponse))
            .when(personService).updatePerson(
                any(PersonUpdateDTO.class),
                eq(firstName),
                eq(lastName)
            );

        mockMvc.perform(put("/person/{theFirstName}-{theLastName}", firstName, lastName)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedPerson)))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.status").value(409))
            .andExpect(jsonPath("$.message").value(errorResponse));
    }

    @Test
    void updatePerson_ResourceNotFound_test() throws Exception {
        String firstName = "Unknown";
        String lastName = "Person";
        PersonUpdateDTO updatedPerson = new PersonUpdateDTO(
            "1234 New Street",
            "NewCity",
            "54321",
            "321-654-9870",
            "john.doe@newemail.com"
        );
        String errorResponse = "Resource not found";

        doThrow(new ResourceNotFoundException(errorResponse))
            .when(personService).updatePerson(
                any(PersonUpdateDTO.class),
                eq(firstName),
                eq(lastName)
            );

        mockMvc.perform(put("/person/{theFirstName}-{theLastName}", firstName, lastName)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedPerson)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.message").value(errorResponse));
    }

    @Test
    void deletePerson_Success_test() throws Exception {
        String firstName = "John";
        String lastName = "Doe";
        String successResponse = "Person deleted successfully";

        doNothing()
            .when(personService).deletePerson(firstName, lastName);

        mockMvc.perform(delete("/person/{theFirstName}-{theLastName}", firstName, lastName))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(200))
            .andExpect(jsonPath("$.message").value(successResponse));
    }

    @Test
    void deletePerson_ResourceNotFound_test() throws Exception {
        String firstName = "Unknown";
        String lastName = "Person";
        String errorResponse = "Resource not found";

        doThrow(new ResourceNotFoundException(errorResponse))
            .when(personService).deletePerson(firstName, lastName);

        mockMvc.perform(delete("/person/{theFirstName}-{theLastName}", firstName, lastName))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.message").value(errorResponse));
    }
}
