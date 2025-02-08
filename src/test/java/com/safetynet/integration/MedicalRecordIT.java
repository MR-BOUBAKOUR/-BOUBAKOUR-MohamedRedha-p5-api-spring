package com.safetynet.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.dto.firestation.FirestationCreateDTO;
import com.safetynet.dto.medicalrecord.MedicalRecordCreateDTO;
import com.safetynet.dto.medicalrecord.MedicalRecordUpdateDTO;

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
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class MedicalRecordIT {

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
    void findAllMedicalRecords_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Boyd"));
    }

    @Test
    void findMedicalRecordByName_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/medicalRecord/{firstName}-{lastName}", "John", "Boyd")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Boyd"));
    }

    @Test
    void findMedicalRecordByName_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/medicalRecord/{firstName}-{lastName}", "Unknown", "Person")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Resource not found"));
    }

    @Test
    void addMedicalRecord_shouldReturnCreated() throws Exception {
        MedicalRecordCreateDTO newMedicalRecord = new MedicalRecordCreateDTO(
                "Jane",
                "Doe",
                "01/01/1990",
                List.of("Medication1"),
                List.of("Allergy1")
        );

        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newMedicalRecord)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.message").value("Medical record added successfully"));
    }

    @Test
    void addMedicalRecord_shouldReturnConflict() throws Exception {
        MedicalRecordCreateDTO existingMedicalRecord = new MedicalRecordCreateDTO(
                "Jacob",
                "Boyd",
                "03/06/1989",
                List.of("pharmacol:5000mg", "terazine:10mg", "noznazol:250mg"),
                List.of()
        );

        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(existingMedicalRecord)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("Resource already exist"));
    }

    @Test
    void addMedicalRecord_shouldReturnValidationError() throws Exception {
        MedicalRecordCreateDTO invalidMedicalRecord = new MedicalRecordCreateDTO(
                "J",
                "D",
                "01-01-1990",
                List.of("Medication1"),
                List.of("Allergy1")
        );

        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidMedicalRecord)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message", containsString("Last name must be between 2 and 50 characters")))
                .andExpect(jsonPath("$.message", containsString("First name must be between 2 and 50 characters")))
                .andExpect(jsonPath("$.message", containsString("Birthdate must be in the format MM/dd/yyyy")));
    }

    @Test
    void updateMedicalRecord_shouldReturnOk() throws Exception {
        MedicalRecordUpdateDTO updateDTO = new MedicalRecordUpdateDTO(
                "03/06/1984",
                List.of("updatedMedications1", "updatedMedications2"),
                List.of("updatedAllergies1")
        );

        mockMvc.perform(put("/medicalRecord/{firstName}-{lastName}", "John", "Boyd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Medical record updated successfully"));
    }

    @Test
    void updateMedicalRecord_shouldReturnNotFound() throws Exception {
        MedicalRecordUpdateDTO updateDTO = new MedicalRecordUpdateDTO(
                "03/06/1984",
                List.of("updatedMedications1", "updatedMedications2"),
                List.of("updatedAllergies1")
        );

        mockMvc.perform(put("/medicalRecord/{firstName}-{lastName}", "Unknown", "Person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Resource not found"));
    }

    @Test
    void updateMedicalRecord_shouldReturnConflict() throws Exception {
        MedicalRecordUpdateDTO updateDTO = new MedicalRecordUpdateDTO(
                "09/06/2017",
                List.of(),
                List.of()
        );

        mockMvc.perform(put("/medicalRecord/{firstName}-{lastName}", "Roger", "Boyd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("The medicalRecord with this name is exactly the same as the one you are trying to update"));
    }

    @Test
    void updateMedicalRecord_shouldReturnValidationError() throws Exception {
        MedicalRecordUpdateDTO invalidMedicalRecord = new MedicalRecordUpdateDTO(
                "01-01-1990",
                List.of("Medication1"),
                List.of("Allergy1")
        );

        mockMvc.perform(put("/medicalRecord/{firstName}-{lastName}", "Roger", "Boyd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidMedicalRecord)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message", containsString("Birthdate must be in the format MM/dd/yyyy")));
    }

    @Test
    void deleteMedicalRecord_shouldReturnOk() throws Exception {
        mockMvc.perform(delete("/medicalRecord/{firstName}-{lastName}", "John", "Boyd")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Medical record deleted successfully"));
    }

    @Test
    void deleteMedicalRecord_shouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/medicalRecord/{firstName}-{lastName}", "Unknown", "Person")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Resource not found"));
    }
}