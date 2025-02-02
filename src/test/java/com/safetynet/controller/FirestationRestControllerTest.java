package com.safetynet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.dto.firestation.FirestationCreateDTO;
import com.safetynet.dto.firestation.FirestationResponseDTO;
import com.safetynet.dto.firestation.FirestationUpdateDTO;
import com.safetynet.exception.ConflictException;
import com.safetynet.exception.ResourceNotFoundException;
import com.safetynet.service.FirestationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

@WebMvcTest(FirestationRestController.class)
class FirestationRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FirestationService firestationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllFirestations_Success_Test() throws Exception {

        List<FirestationResponseDTO> firestations = List.of(
                new FirestationResponseDTO("123 Main St", 1),
                new FirestationResponseDTO("456 Elm St", 2)
        );

        when(firestationService.findAllFirestations())
                .thenReturn(firestations);

        mockMvc.perform(get("/firestation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(firestations.size()))
                .andExpect(jsonPath("$[0].address").value("123 Main St"))
                .andExpect(jsonPath("$[0].station").value("1"))
                .andExpect(jsonPath("$[1].address").value("456 Elm St"))
                .andExpect(jsonPath("$[1].station").value("2"));
    }

    @Test
    void getFirestationByAddress_Success_Test() throws Exception {

        String theAddress = "123 Main St";
        FirestationResponseDTO firestation = new FirestationResponseDTO(theAddress, 1);

        when(firestationService.findFirestationByAddress(theAddress))
                .thenReturn(firestation);

        mockMvc.perform(get("/firestation/{theAddress}", theAddress)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value(theAddress))
                .andExpect(jsonPath("$.station").value(1));
    }

    @Test
    void getFirestationByAddress_ResourceNotFound_Test() throws Exception {
        String theAddress = "999 Unknown St";
        String errorResponse = "Resource not found";

        when(firestationService.findFirestationByAddress(theAddress))
                .thenThrow(new ResourceNotFoundException(errorResponse));

        mockMvc.perform(get("/firestation/{theAddress}", theAddress))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value(errorResponse));
    }

    @Test
    void addFirestation_Success_Test() throws Exception {

        FirestationCreateDTO newFirestation = new FirestationCreateDTO("789 Maple St", 3);
        String successResponse = "Firestation added successfully";

        doNothing()
                .when(firestationService).addFirestation(any(FirestationCreateDTO.class));

        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newFirestation)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.message").value(successResponse));
    }

    @Test
    void addFirestation_Conflict_Test() throws Exception {

        FirestationCreateDTO existingFirestation = new FirestationCreateDTO("123 Main St", 1);
        String errorResponse = "Resource already exist";

        doThrow(new ConflictException(errorResponse))
                .when(firestationService).addFirestation(any(FirestationCreateDTO.class));

        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(existingFirestation)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value(errorResponse));
    }

    @Test
    void updateFirestation_Success_Test() throws Exception {
        String theAddress = "123 Main St";
        FirestationUpdateDTO updatedFirestation = new FirestationUpdateDTO(5);
        String successResponse = "Firestation updated successfully";

        doNothing()
                .when(firestationService).updateFirestation(
                        any(FirestationUpdateDTO.class),
                        eq(theAddress)
                );

        mockMvc.perform(put("/firestation/{address}", theAddress)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedFirestation)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value(successResponse));
    }

    @Test
    void updateFirestation_ResourceNotFound_Test() throws Exception {

        String theAddress = "999 Unknown St";
        FirestationUpdateDTO updatedFirestation = new FirestationUpdateDTO(5);
        String errorResponse = "Resource not found";

        doThrow(new ResourceNotFoundException(errorResponse))
                .when(firestationService).updateFirestation(
                        any(FirestationUpdateDTO.class),
                        eq(theAddress)
                );

        mockMvc.perform(put("/firestation/{address}", theAddress)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedFirestation)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value(errorResponse));
    }

    @Test
    void updateFirestation_Conflict_Test() throws Exception {

        String theAddress = "123 Main St";
        FirestationUpdateDTO updatedFirestation = new FirestationUpdateDTO(5);
        String errorResponse = "The firestation with this address is exactly the same as the one you are trying to update";

        doThrow(new ConflictException(errorResponse))
                .when(firestationService).updateFirestation(
                        any(FirestationUpdateDTO.class),
                        eq(theAddress)
                );

        mockMvc.perform(put("/firestation/{address}", theAddress)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedFirestation)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value(errorResponse));
    }

    @Test
    void deleteFirestation_Success_Test() throws Exception {

        String theAddress = "123 Main St";
        String successResponse = "Firestation deleted successfully";

        doNothing()
                .when(firestationService).deleteFirestation(theAddress);

        mockMvc.perform(delete("/firestation/{address}", theAddress))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value(successResponse));
    }

    @Test
    void deleteFirestation_ResourceNotFound_Test() throws Exception {
        String theAddress = "999 Unknown St";
        String errorResponse = "Resource not found";

        doThrow(new ResourceNotFoundException(errorResponse))
                .when(firestationService).deleteFirestation(theAddress);

        mockMvc.perform(delete("/firestation/{theAddress}", theAddress))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value(errorResponse));
    }
}