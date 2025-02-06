package com.safetynet.service;

import com.safetynet.dto.firestation.FirestationCreateDTO;
import com.safetynet.dto.firestation.FirestationResponseDTO;
import com.safetynet.dto.firestation.FirestationUpdateDTO;
import com.safetynet.exception.ConflictException;
import com.safetynet.exception.ResourceNotFoundException;
import com.safetynet.mapper.FirestationMapper;
import com.safetynet.model.Firestation;
import com.safetynet.repository.DataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class FirestationServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(FirestationServiceTest.class);

    @Mock
    private DataRepository dataRepository;

    @Mock
    private FirestationMapper firestationMapper;

    @InjectMocks
    private FirestationService firestationService;

    @BeforeEach
    void setUp() {

        List<Firestation> firestations = new ArrayList<>();
        firestations.add(new Firestation("123 Main St", 1));
        firestations.add(new Firestation("456 Elm St", 2));

        when(dataRepository.getFirestations()).thenReturn(firestations);
        firestationService = new FirestationService(dataRepository, firestationMapper);
    }

    @Test
    void findAllFirestations_shouldReturnFirestations_test() {

        when(firestationMapper.toResponseDTO(any(Firestation.class)))
                .thenReturn(new FirestationResponseDTO("123 Main St", 1));

        List<FirestationResponseDTO> result = firestationService.findAllFirestations();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(dataRepository, times(2)).getFirestations();
    }

    @Test
    void findFirestationByAddress_shouldReturnFirestationResponseDTO_test() {

        Firestation firestation = new Firestation("123 Main St", 1);

        when(firestationMapper.toResponseDTO(firestation))
                .thenReturn(new FirestationResponseDTO("123 Main St", 1));

        FirestationResponseDTO result = firestationService.findFirestationByAddress("123 Main St");

        assertNotNull(result);
        assertEquals("123 Main St", result.getAddress());
    }

    @Test
    void findFirestationByAddress_whenNotFound_shouldThrowResourceNotFoundException_test() {

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            firestationService.findFirestationByAddress("999 Nonexistent St");
        });

        assertEquals("Resource not found", exception.getMessage());
    }

    @Test
    void addFirestation_shouldAddFirestation_test() {

        FirestationCreateDTO firestationCreateDTO = new FirestationCreateDTO("789 Pine St", 3);

        firestationService.addFirestation(firestationCreateDTO);

        verify(dataRepository, times(1)).writeData(eq("firestations"), anyList());
    }

    @Test
    void addFirestation_whenFirestationExists_shouldThrowConflictException_test() {

        FirestationCreateDTO firestationCreateDTO = new FirestationCreateDTO("123 Main St", 1);

        ConflictException exception = assertThrows(ConflictException.class, () -> {
            firestationService.addFirestation(firestationCreateDTO);
        });

        assertEquals("Resource already exist", exception.getMessage());
    }

    @Test
    void updateFirestation_shouldUpdateFirestation_test() {

        FirestationUpdateDTO firestationUpdateDTO = new FirestationUpdateDTO(3);
        Firestation firestation = new Firestation("123 Main St", 1);

        when(firestationMapper.toEntityFromUpdateDTO(firestationUpdateDTO)).thenReturn(new Firestation("123 Main St", 3));

        firestationService.updateFirestation(firestationUpdateDTO, "123 Main St");

        verify(dataRepository, times(1)).writeData(eq("firestations"), anyList());
    }

    @Test
    void updateFirestation_whenNotFound_shouldThrowResourceNotFoundException_test() {

        FirestationUpdateDTO firestationUpdateDTO = new FirestationUpdateDTO(3);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            firestationService.updateFirestation(firestationUpdateDTO, "999 Nonexistent St");
        });

        assertEquals("Resource not found", exception.getMessage());
    }

    @Test
    void deleteFirestation_shouldDeleteFirestation_test() {

        firestationService.deleteFirestation("123 Main St");

        verify(dataRepository, times(1)).writeData(eq("firestations"), anyList());
    }

    @Test
    void deleteFirestation_whenNotFound_shouldThrowResourceNotFoundException_test() {

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            firestationService.deleteFirestation("999 Nonexistent St");
        });

        assertEquals("Resource not found", exception.getMessage());
    }
}
