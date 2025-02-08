package com.safetynet.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.dto.person.PersonCreateDTO;
import com.safetynet.dto.person.PersonUpdateDTO;
import com.safetynet.repository.DataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class PersonIT {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        Path testFile = Path.of("src/test/resources/data-test.json");
        Path tempFile = Path.of("src/test/resources/data-test-temp.json");

        Files.copy(testFile, tempFile, StandardCopyOption.REPLACE_EXISTING);

        DataRepository dataRepository = new DataRepository();
        DataRepository.FILE_PATH = tempFile.toString();

        dataRepository.init();
    }

    @Test
    void findAllPersons_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/person")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Boyd"));
    }

    @Test
    void findPersonByName_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/person/{firstName}-{lastName}", "Foster", "Shepard")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Foster"))
                .andExpect(jsonPath("$.lastName").value("Shepard"));
    }

    @Test
    void findPersonByName_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/person/{firstName}-{lastName}", "Unknown", "Person")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Resource not found"));
    }

    @Test
    void addPerson_shouldReturnCreated() throws Exception {
        PersonCreateDTO newPerson = new PersonCreateDTO(
                "Jane", "Doe", "123 Main St", "New York", "10001", "555-123-9999", "jane.doe@example.com"
        );

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPerson)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.message").value("Person added successfully"));
    }

    @Test
    void addPerson_shouldReturnConflict() throws Exception {
        PersonCreateDTO existingPerson = new PersonCreateDTO(
                "Tony", "Cooper", "112 Steppes Pl", "Culver", "97451", "841-874-6874", "tcoop@ymail.com"
        );

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(existingPerson)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("Resource already exist"));
    }

    @Test
    void addPerson_shouldReturnValidationError() throws Exception {
        PersonCreateDTO invalidPerson = new PersonCreateDTO(
                "",
                "",
                "",
                "",
                "1234",
                "555-1234",
                "invalid-email"
        );

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPerson)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message", containsString("First name cannot be empty")))
                .andExpect(jsonPath("$.message", containsString("Last name cannot be empty")))
                .andExpect(jsonPath("$.message", containsString("Address cannot be empty")))
                .andExpect(jsonPath("$.message", containsString("City cannot be empty")))
                .andExpect(jsonPath("$.message", containsString("Zip code must be exactly 5 digits")))
                .andExpect(jsonPath("$.message", containsString("Phone number must follow the format xxx-xxx-xxxx")))
                .andExpect(jsonPath("$.message", containsString("Email should be valid")));
    }

    @Test
    void updatePerson_shouldReturnOk() throws Exception {
        PersonUpdateDTO updateDTO = new PersonUpdateDTO(
                "Updated St", "Updated City", "12345", "999-999-9999", "updated.email@example.com"
        );

        mockMvc.perform(put("/person/{firstName}-{lastName}", "Allison", "Boyd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Person updated successfully"));
    }

    @Test
    void updatePerson_shouldReturnNotFound() throws Exception {
        PersonUpdateDTO updateDTO = new PersonUpdateDTO(
                "Updated St", "Updated City", "12345", "999-999-9999", "updated.email@example.com"
        );

        mockMvc.perform(put("/person/{firstName}-{lastName}", "Unknown", "Person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Resource not found"));
    }

    @Test
    void updatePerson_shouldReturnConflict() throws Exception {
        PersonUpdateDTO updateDTO = new PersonUpdateDTO(
                "947 E. Rose Dr", "Culver", "97451", "841-874-7784", "bstel@email.com"
        );

        mockMvc.perform(put("/person/{firstName}-{lastName}", "Kendrik", "Stelzer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("The person with this name is exactly the same as the one you are trying to update"));
    }

    @Test
    void updatePerson_shouldReturnValidationError() throws Exception {
        PersonUpdateDTO invalidPerson = new PersonUpdateDTO(
                "",
                "",
                "1234",
                "555-1234",
                "invalid-email"
        );

        mockMvc.perform(put("/person/{firstName}-{lastName}", "Brian", "Stelzer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPerson)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message", containsString("Address cannot be empty")))
                .andExpect(jsonPath("$.message", containsString("City cannot be empty")))
                .andExpect(jsonPath("$.message", containsString("Zip code must be exactly 5 digits")))
                .andExpect(jsonPath("$.message", containsString("Phone number must follow the format xxx-xxx-xxxx")))
                .andExpect(jsonPath("$.message", containsString("Email should be valid")));
    }

    @Test
    void deletePerson_shouldReturnOk() throws Exception {
        mockMvc.perform(delete("/person/{firstName}-{lastName}", "John", "Boyd")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Person deleted successfully"));
    }

    @Test
    void deletePerson_shouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/person/{firstName}-{lastName}", "Unknown", "Person")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Resource not found"));
    }
}