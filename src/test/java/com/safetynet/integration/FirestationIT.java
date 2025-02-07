package com.safetynet.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.dto.firestation.FirestationCreateDTO;
import com.safetynet.dto.firestation.FirestationUpdateDTO;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class FirestationIT {

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
    void findAllFirestations_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/firestation")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].address").value("1509 Culver St"))
                .andExpect(jsonPath("$[1].address").value("29 15th St"))
                .andExpect(jsonPath("$[2].address").value("834 Binoc Ave"));
    }

    @Test
    void findFirestationByAddress_shouldReturnOk() throws Exception {
        String address = "1509 Culver St";

        mockMvc.perform(get("/firestation/{address}", address)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value(address))
                .andExpect(jsonPath("$.station").value(3));
    }

    @Test
    void findFirestationByAddress_shouldReturnNotFound() throws Exception {
        String address = "Nonexistent Address";

        mockMvc.perform(get("/firestation/{address}", address)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Resource not found"));
    }

    @Test
    void addFirestation_shouldReturnCreated() throws Exception {
        FirestationCreateDTO newFirestation = new FirestationCreateDTO("789 Maple St", 3);

        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newFirestation)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.message").value("Firestation added successfully"));
    }

    @Test
    void addFirestation_shouldReturnConflict() throws Exception {
        FirestationCreateDTO alreadyExistDTO = new FirestationCreateDTO("29 15th St",2); // No change in station number

        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(alreadyExistDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("Resource already exist"));
    }

    @Test
    void addFirestation_shouldReturnValidationError() throws Exception {
        FirestationCreateDTO invalidDTO = new FirestationCreateDTO("add", 5); // Invalid address

        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Address must be between 5 and 255 characters"));
    }

    @Test
    void updateFirestation_shouldReturnOk() throws Exception {
        String address = "1509 Culver St";
        FirestationUpdateDTO updateDTO = new FirestationUpdateDTO(2);

        mockMvc.perform(put("/firestation/{address}", address)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Firestation updated successfully"));
    }

    @Test
    void updateFirestation_shouldReturnNotFound() throws Exception {
        String address = "Nonexistent Address";
        FirestationUpdateDTO updateDTO = new FirestationUpdateDTO(2);

        mockMvc.perform(put("/firestation/{address}", address)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Resource not found"));
    }

    @Test
    void updateFirestation_shouldReturnConflict() throws Exception {
        String address = "29 15th St";
        FirestationUpdateDTO alreadyExistDTO = new FirestationUpdateDTO(2); // No change in station number

        mockMvc.perform(put("/firestation/{address}", address)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(alreadyExistDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("The firestation with this address is exactly the same as the one you are trying to update"));
    }

    @Test
    void updateFirestation_shouldReturnValidationError() throws Exception {
        String address = "1509 Culver St";
        FirestationUpdateDTO invalidDTO = new FirestationUpdateDTO(1000); // Invalid station number

        mockMvc.perform(put("/firestation/{address}", address)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Station number must be less than 1000"));
    }

    @Test
    void deleteFirestation_shouldReturnOk() throws Exception {
        String address = "908 73rd St";

        mockMvc.perform(delete("/firestation/{address}", address)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Firestation deleted successfully"));
    }

    @Test
    void deleteFirestation_shouldReturnNotFound() throws Exception {
        String address = "Nonexistent Address";

        mockMvc.perform(delete("/firestation/{address}", address)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Resource not found"));
    }

}