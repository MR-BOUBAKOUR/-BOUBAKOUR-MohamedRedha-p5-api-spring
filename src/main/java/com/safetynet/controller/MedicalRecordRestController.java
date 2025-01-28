package com.safetynet.controller;

import com.safetynet.dto.medicalrecord.MedicalRecordCreateDTO;
import com.safetynet.dto.medicalrecord.MedicalRecordResponseDTO;
import com.safetynet.dto.medicalrecord.MedicalRecordUpdateDTO;
import com.safetynet.model.MedicalRecord;
import com.safetynet.service.MedicalRecordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MedicalRecordRestController {

    @Autowired
    MedicalRecordService medicalRecordService;

    @GetMapping("/medicalrecord")
    public List<MedicalRecordResponseDTO> getAllMedicalrecords() {
        return medicalRecordService.findAllMedicalRecords();
    }

    @GetMapping("/medicalrecord/{theFirstName}-{theLastName}")
    public MedicalRecordResponseDTO getMedicalrecord(@PathVariable String theFirstName, @PathVariable String theLastName) {
        return medicalRecordService.findMedicalrecordByFirstNameAndLastName(theFirstName, theLastName);
    }

    @PostMapping("/medicalrecord")
    public void addMedicalrecord(@Valid @RequestBody MedicalRecordCreateDTO theMedicalrecord) {
        medicalRecordService.addMedicalrecord(theMedicalrecord);
    }

    @PutMapping("/medicalrecord/{theFirstName}-{theLastName}")
    public void updateMedicalrecord(@Valid @RequestBody MedicalRecordUpdateDTO theMedicalrecord, @PathVariable String theFirstName, @PathVariable String theLastName) {
        medicalRecordService.updateMedicalrecord(theMedicalrecord, theFirstName, theLastName);
    }

    @DeleteMapping("/medicalrecord/{theFirstName}-{theLastName}")
    public void deleteMedicalrecord(@PathVariable String theFirstName, @PathVariable String theLastName) {
        medicalRecordService.deleteMedicalrecord(theFirstName, theLastName);
    }

}
