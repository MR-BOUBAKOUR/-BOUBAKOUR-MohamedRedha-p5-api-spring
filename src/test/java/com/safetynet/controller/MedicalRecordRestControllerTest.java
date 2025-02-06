package com.safetynet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.dto.medicalrecord.MedicalRecordCreateDTO;
import com.safetynet.dto.medicalrecord.MedicalRecordResponseDTO;
import com.safetynet.dto.medicalrecord.MedicalRecordUpdateDTO;
import com.safetynet.exception.ConflictException;
import com.safetynet.exception.ResourceNotFoundException;
import com.safetynet.service.MedicalRecordService;
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

@WebMvcTest(MedicalRecordRestController.class)
class MedicalRecordRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MedicalRecordService medicalRecordService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllMedicalRecords_Success_test() throws Exception {
        List<MedicalRecordResponseDTO> medicalRecords = List.of(
            new MedicalRecordResponseDTO(
                "John",
                "Doe",
                "01/01/1990",
                List.of("Medication1"),
                List.of("Allergy1")),
            new MedicalRecordResponseDTO(
                "Jane",
                "Smith",
                "02/02/1985",
                List.of("Medication2"),
                List.of("Allergy2"))
        );

        when(medicalRecordService.findAllMedicalRecords())
            .thenReturn(medicalRecords);

        mockMvc.perform(get("/medicalRecord"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(medicalRecords.size()))
            .andExpect(jsonPath("$[0].firstName").value("John"))
            .andExpect(jsonPath("$[0].lastName").value("Doe"))
            .andExpect(jsonPath("$[1].firstName").value("Jane"))
            .andExpect(jsonPath("$[1].lastName").value("Smith"));
    }

    @Test
    void getMedicalRecordByFirstNameAndLastName_Success_test() throws Exception {
        String firstName = "John";
        String lastName = "Doe";
        MedicalRecordResponseDTO medicalRecord = new MedicalRecordResponseDTO(
            firstName,
            lastName,
            "01/01/1990",
            List.of("Medication1"),
            List.of("Allergy1")
        );

        when(medicalRecordService.findMedicalrecordByFirstNameAndLastName(firstName, lastName))
            .thenReturn(medicalRecord);

        mockMvc.perform(get("/medicalRecord/{theFirstName}-{theLastName}", firstName, lastName))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value(firstName))
            .andExpect(jsonPath("$.lastName").value(lastName));
    }

    @Test
    void getMedicalRecordByFirstNameAndLastName_ResourceNotFound_test() throws Exception {
        String firstName = "Unknown";
        String lastName = "Person";
        String errorResponse = "Resource not found";

        when(medicalRecordService.findMedicalrecordByFirstNameAndLastName(firstName, lastName))
            .thenThrow(new ResourceNotFoundException(errorResponse));

        mockMvc.perform(get("/medicalRecord/{theFirstName}-{theLastName}", firstName, lastName))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.message").value(errorResponse));
    }

    @Test
    void addMedicalRecord_Success_test() throws Exception {
        MedicalRecordCreateDTO newMedicalRecord = new MedicalRecordCreateDTO(
            "John",
            "Doe",
            "01/01/1990",
            List.of("Medication1"),
            List.of("Allergy1")
        );
        String successResponse = "Medical record added successfully";

        doNothing()
                .when(medicalRecordService).addMedicalrecord(any(MedicalRecordCreateDTO.class));

        mockMvc.perform(post("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newMedicalRecord)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.status").value(201))
            .andExpect(jsonPath("$.message").value(successResponse));
    }

    @Test
    void addMedicalrecord_Conflict_test() throws Exception {
        MedicalRecordCreateDTO existingMedicalRecord = new MedicalRecordCreateDTO(
                "John",
                "Boyd",
                "03/06/1984",
                List.of("aznol:350mg", "hydrapermazol:100mg"),
                List.of("nillacilan")
        );
        String errorResponse = "Resource already exists";

        doThrow(new ConflictException(errorResponse))
                .when(medicalRecordService).addMedicalrecord(any(MedicalRecordCreateDTO.class));

        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(existingMedicalRecord)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value(errorResponse));
    }

    @Test
    void updateMedicalRecord_Success_test() throws Exception {
        String firstName = "John";
        String lastName = "Doe";
        MedicalRecordUpdateDTO updatedMedicalRecord = new MedicalRecordUpdateDTO(
            "01/01/1990",
            List.of("Medication3"),
            List.of("Allergy3")
        );
        String successResponse = "Medical record updated successfully";

        doNothing()
            .when(medicalRecordService).updateMedicalrecord(
                any(MedicalRecordUpdateDTO.class),
                eq(firstName),
                eq(lastName)
            );

        mockMvc.perform(put("/medicalRecord/{theFirstName}-{theLastName}", firstName, lastName)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedMedicalRecord)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(200))
            .andExpect(jsonPath("$.message").value(successResponse));
    }

    @Test
    void updateMedicalRecord_Conflict_test() throws Exception {
        String firstName = "John";
        String lastName = "Boyd";
        MedicalRecordUpdateDTO updatedMedicalRecord = new MedicalRecordUpdateDTO(
                "03/06/1984",
                List.of("aznol:350mg", "hydrapermazol:100mg"),
                List.of("nillacilan")
        );
        String errorResponse = "The medical record for this patient is exactly the same as the one you are trying to update";

        doThrow(new ConflictException(errorResponse))
                .when(medicalRecordService).updateMedicalrecord(
                        any(MedicalRecordUpdateDTO.class),
                        eq(firstName),
                        eq(lastName)
                );

        mockMvc.perform(put("/medicalRecord/{theFirstName}-{theLastName}", firstName, lastName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedMedicalRecord)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value(errorResponse));
    }

    @Test
    void updateMedicalRecord_ResourceNotFound_test() throws Exception {
        String firstName = "Unknown";
        String lastName = "Person";
        MedicalRecordUpdateDTO updatedMedicalRecord = new MedicalRecordUpdateDTO(
            "01/01/1990",
            List.of("Medication404"),
            List.of("Allergy404")
        );
        String errorResponse = "Resource not found";

        doThrow(new ResourceNotFoundException(errorResponse))
            .when(medicalRecordService).updateMedicalrecord(
                any(MedicalRecordUpdateDTO.class),
                eq(firstName),
                eq(lastName)
            );

        mockMvc.perform(put("/medicalRecord/{theFirstName}-{theLastName}", firstName, lastName)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedMedicalRecord)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.message").value(errorResponse));
    }

    @Test
    void deleteMedicalRecord_Success_test() throws Exception {
        String firstName = "John";
        String lastName = "Doe";
        String successResponse = "Medical record deleted successfully";

        doNothing()
            .when(medicalRecordService).deleteMedicalrecord(firstName, lastName);

        mockMvc.perform(delete("/medicalRecord/{theFirstName}-{theLastName}", firstName, lastName))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(200))
            .andExpect(jsonPath("$.message").value(successResponse));
    }

    @Test
    void deleteMedicalRecord_ResourceNotFound_test() throws Exception {
        String firstName = "Unknown";
        String lastName = "Person";
        String errorResponse = "Resource not found";

        doThrow(new ResourceNotFoundException(errorResponse))
            .when(medicalRecordService).deleteMedicalrecord(firstName, lastName);

        mockMvc.perform(delete("/medicalRecord/{theFirstName}-{theLastName}", firstName, lastName))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.message").value(errorResponse));
    }
}
