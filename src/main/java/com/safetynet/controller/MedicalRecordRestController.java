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

@RestController
public class MedicalRecordRestController {

    private final MedicalRecordService medicalRecordService;

    @Autowired
    public MedicalRecordRestController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @GetMapping("/medicalRecord")
    public List<MedicalRecordResponseDTO> getAllMedicalrecords() {
        return medicalRecordService.findAllMedicalRecords();
    }

    @GetMapping("/medicalRecord/{theFirstName}-{theLastName}")
    public MedicalRecordResponseDTO getMedicalrecord(@PathVariable String theFirstName, @PathVariable String theLastName) {
        return medicalRecordService.findMedicalrecordByFirstNameAndLastName(theFirstName, theLastName);
    }

    @PostMapping("/medicalRecord")
    public ResponseEntity<SuccessResponse> addMedicalrecord(@Valid @RequestBody MedicalRecordCreateDTO theMedicalrecord) {
        medicalRecordService.addMedicalrecord(theMedicalrecord);
        SuccessResponse response = new SuccessResponse(HttpStatus.CREATED.value(), "Medical record added successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/medicalRecord/{theFirstName}-{theLastName}")
    public ResponseEntity<SuccessResponse> updateMedicalrecord(@Valid @RequestBody MedicalRecordUpdateDTO theMedicalrecord, @PathVariable String theFirstName, @PathVariable String theLastName) {
        medicalRecordService.updateMedicalrecord(theMedicalrecord, theFirstName, theLastName);
        SuccessResponse response = new SuccessResponse(HttpStatus.OK.value(), "Medical record updated successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/medicalRecord/{theFirstName}-{theLastName}")
    public ResponseEntity<SuccessResponse> deleteMedicalrecord(@PathVariable String theFirstName, @PathVariable String theLastName) {
        medicalRecordService.deleteMedicalrecord(theFirstName, theLastName);
        SuccessResponse response = new SuccessResponse(HttpStatus.OK.value(), "Medical record deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}