package com.safetynet.controller;

import com.safetynet.dto.medicalrecord.MedicalRecordCreateDTO;
import com.safetynet.dto.medicalrecord.MedicalRecordResponseDTO;
import com.safetynet.dto.medicalrecord.MedicalRecordUpdateDTO;
import com.safetynet.model.MedicalRecord;
import com.safetynet.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MedicalRecordRestController {

    @Autowired
    MedicalRecordService medicalRecordService;

    @GetMapping("/medicalRecord")
    public List<MedicalRecordResponseDTO> getAllMedicalrecords() {
        return medicalRecordService.findAllMedicalRecords();
    }

    @GetMapping("/medicalRecord/{theFirstName}-{theLastName}")
    public MedicalRecordResponseDTO getMedicalrecord(@PathVariable String theFirstName, @PathVariable String theLastName) {
        return medicalRecordService.findMedicalrecordByFirstNameAndLastName(theFirstName, theLastName);
    }

    @PostMapping("/medicalRecord")
    public void addMedicalrecord(@RequestBody MedicalRecordCreateDTO theMedicalrecord) {
        medicalRecordService.addMedicalrecord(theMedicalrecord);
    }

    @PutMapping("/medicalRecord/{theFirstName}-{theLastName}")
    public void updateMedicalrecord(@RequestBody MedicalRecordUpdateDTO theMedicalrecord, @PathVariable String theFirstName, @PathVariable String theLastName) {
        medicalRecordService.updateMedicalrecord(theMedicalrecord, theFirstName, theLastName);
    }

    @DeleteMapping("/medicalRecord/{theFirstName}-{theLastName}")
    public void deleteMedicalrecord(@PathVariable String theFirstName, @PathVariable String theLastName) {
        medicalRecordService.deleteMedicalrecord(theFirstName, theLastName);
    }

}
