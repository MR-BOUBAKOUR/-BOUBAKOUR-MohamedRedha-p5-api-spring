package com.safetynet.controller;

import com.safetynet.dto.SuccessResponse;
import com.safetynet.dto.medicalrecord.MedicalRecordCreateDTO;
import com.safetynet.dto.medicalrecord.MedicalRecordResponseDTO;
import com.safetynet.dto.medicalrecord.MedicalRecordUpdateDTO;
import com.safetynet.service.MedicalRecordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type Medical record rest controller.
 */
@RestController
public class MedicalRecordRestController {

    private final MedicalRecordService medicalRecordService;

    /**
     * Instantiates a new Medical record rest controller.
     *
     * @param medicalRecordService the medical record service
     */
    @Autowired
    public MedicalRecordRestController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    /**
     * Gets all medicalrecords.
     *
     * @return the all medicalrecords
     */
    @GetMapping("/medicalRecord")
    public List<MedicalRecordResponseDTO> getAllMedicalrecords() {
        return medicalRecordService.findAllMedicalRecords();
    }

    /**
     * Gets medicalrecord.
     *
     * @param theFirstName the first name
     * @param theLastName  the last name
     * @return the medicalrecord
     */
    @GetMapping("/medicalRecord/{theFirstName}-{theLastName}")
    public MedicalRecordResponseDTO getMedicalrecord(@PathVariable String theFirstName, @PathVariable String theLastName) {
        return medicalRecordService.findMedicalrecordByFirstNameAndLastName(theFirstName, theLastName);
    }

    /**
     * Add medicalrecord response entity.
     *
     * @param theMedicalrecord the medicalrecord
     * @return the response entity
     */
    @PostMapping("/medicalRecord")
    public ResponseEntity<SuccessResponse> addMedicalrecord(@Valid @RequestBody MedicalRecordCreateDTO theMedicalrecord) {
        medicalRecordService.addMedicalrecord(theMedicalrecord);
        SuccessResponse response = new SuccessResponse(HttpStatus.CREATED.value(), "Medical record added successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Update medicalrecord response entity.
     *
     * @param theMedicalrecord the medicalrecord
     * @param theFirstName     the first name
     * @param theLastName      the last name
     * @return the response entity
     */
    @PutMapping("/medicalRecord/{theFirstName}-{theLastName}")
    public ResponseEntity<SuccessResponse> updateMedicalrecord(@Valid @RequestBody MedicalRecordUpdateDTO theMedicalrecord, @PathVariable String theFirstName, @PathVariable String theLastName) {
        medicalRecordService.updateMedicalrecord(theMedicalrecord, theFirstName, theLastName);
        SuccessResponse response = new SuccessResponse(HttpStatus.OK.value(), "Medical record updated successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Delete medicalrecord response entity.
     *
     * @param theFirstName the first name
     * @param theLastName  the last name
     * @return the response entity
     */
    @DeleteMapping("/medicalRecord/{theFirstName}-{theLastName}")
    public ResponseEntity<SuccessResponse> deleteMedicalrecord(@PathVariable String theFirstName, @PathVariable String theLastName) {
        medicalRecordService.deleteMedicalrecord(theFirstName, theLastName);
        SuccessResponse response = new SuccessResponse(HttpStatus.OK.value(), "Medical record deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}