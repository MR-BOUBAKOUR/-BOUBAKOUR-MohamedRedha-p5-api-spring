package com.safetynet.service;

import com.safetynet.dto.medicalrecord.MedicalRecordCreateDTO;
import com.safetynet.dto.medicalrecord.MedicalRecordResponseDTO;
import com.safetynet.dto.medicalrecord.MedicalRecordUpdateDTO;
import com.safetynet.exception.ConflictException;
import com.safetynet.exception.ResourceNotFoundException;
import com.safetynet.mapper.MedicalRecordMapper;
import com.safetynet.model.MedicalRecord;
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
class MedicalRecordServiceTest {

    @Mock
    private DataRepository dataRepository;

    @Mock
    private MedicalRecordMapper medicalRecordMapper;

    @InjectMocks
    private MedicalRecordService medicalRecordService;

    @BeforeEach
    void setUp() {
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        medicalRecords.add(new MedicalRecord("John", "Doe", "01/01/1990", List.of("Aspirin"), List.of("Peanuts")));
        medicalRecords.add(new MedicalRecord("Jane", "Doe", "02/02/1995", List.of("Ibuprofen"), List.of("Pollen")));

        when(dataRepository.getMedicalRecords()).thenReturn(medicalRecords);
        medicalRecordService = new MedicalRecordService(dataRepository, medicalRecordMapper);
    }

    @Test
    void findAllMedicalRecords_shouldReturnMedicalRecords_test() {

        when(medicalRecordMapper.toResponseDTO(any(MedicalRecord.class)))
                .thenReturn(new MedicalRecordResponseDTO("John", "Doe", "01/01/1990", List.of("Aspirin"), List.of("Peanuts")));

        List<MedicalRecordResponseDTO> result = medicalRecordService.findAllMedicalRecords();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(dataRepository, times(2)).getMedicalRecords();
    }

    @Test
    void findMedicalrecordByFirstNameAndLastName_shouldReturnMedicalRecordResponseDTO_test() {

        MedicalRecord medicalRecord = new MedicalRecord("John", "Doe", "01/01/1990", List.of("Aspirin"), List.of("Peanuts"));

        when(medicalRecordMapper.toResponseDTO(medicalRecord))
                .thenReturn(new MedicalRecordResponseDTO("John", "Doe", "01/01/1990", List.of("Aspirin"), List.of("Peanuts")));

        MedicalRecordResponseDTO result = medicalRecordService.findMedicalrecordByFirstNameAndLastName("John", "Doe");

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("01/01/1990", result.getBirthdate());
        assertEquals(List.of("Aspirin"), result.getMedications());
        assertEquals(List.of("Peanuts"), result.getAllergies());
        verify(dataRepository, times(2)).getMedicalRecords();
    }

    @Test
    void findMedicalrecordByFirstNameAndLastName_whenNotFound_shouldThrowResourceNotFoundException_test() {

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            medicalRecordService.findMedicalrecordByFirstNameAndLastName("Nonexistent", "Person");
        });

        assertEquals("Resource not found", exception.getMessage());
    }

    @Test
    void addMedicalrecord_shouldAddMedicalRecord_test() {

        MedicalRecordCreateDTO medicalRecordCreateDTO = new MedicalRecordCreateDTO("Alice", "Smith", "03/03/2000", List.of("Penicillin"), List.of("Dust"));

        when(medicalRecordMapper.toEntityFromCreateDTO(medicalRecordCreateDTO))
                .thenReturn(new MedicalRecord("Susan", "Smith", "05/03/2019", List.of("Penicillin"), List.of("Dust")));

        medicalRecordService.addMedicalrecord(medicalRecordCreateDTO);

        verify(dataRepository, times(1)).writeData(eq("medicalrecords"), anyList());
    }

    @Test
    void addMedicalrecord_whenMedicalRecordExists_shouldThrowConflictException_test() {

        MedicalRecordCreateDTO createDTO = new MedicalRecordCreateDTO("John", "Doe", "01/01/1990", List.of("Aspirin"), List.of("Peanuts"));

        ConflictException exception = assertThrows(ConflictException.class, () -> {
            medicalRecordService.addMedicalrecord(createDTO);
        });

        assertEquals("Resource already exist", exception.getMessage());
    }

    @Test
    void updateMedicalrecord_shouldUpdateMedicalRecord_test() {
        MedicalRecordUpdateDTO medicalRecordUpdateDTO = new MedicalRecordUpdateDTO("05/05/1992", List.of("Tylenol"), List.of("Cats"));

        when(medicalRecordMapper.toEntityFromUpdateDTO(medicalRecordUpdateDTO))
                .thenReturn(new MedicalRecord("John", "Doe", "05/05/1992", List.of("Tylenol"), List.of("Cats")));

        medicalRecordService.updateMedicalrecord(medicalRecordUpdateDTO, "John", "Doe");

        verify(dataRepository, times(1)).writeData(eq("medicalRecords"), anyList());
    }

    @Test
    void updateMedicalrecord_whenNotFound_shouldThrowResourceNotFoundException_test() {

        MedicalRecordUpdateDTO updateDTO = new MedicalRecordUpdateDTO("05/05/1992", List.of("Tylenol"), List.of("Cats"));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            medicalRecordService.updateMedicalrecord(updateDTO, "Nonexistent", "Person");
        });

        assertEquals("Resource not found", exception.getMessage());
    }

    @Test
    void deleteMedicalrecord_shouldDeleteMedicalRecord_test() {

        medicalRecordService.deleteMedicalrecord("John", "Doe");

        verify(dataRepository, times(1)).writeData(eq("medicalRecord"), anyList());
    }

    @Test
    void deleteMedicalrecord_whenNotFound_shouldThrowResourceNotFoundException_test() {

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            medicalRecordService.deleteMedicalrecord("Nonexistent", "Person");
        });

        assertEquals("Resource not found", exception.getMessage());
    }
}
