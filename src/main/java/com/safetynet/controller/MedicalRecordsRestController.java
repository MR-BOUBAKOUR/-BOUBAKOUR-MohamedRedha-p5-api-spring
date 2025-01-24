package com.safetynet.controller;

import com.safetynet.model.MedicalRecord;
import com.safetynet.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MedicalRecordsRestController {

    @Autowired
    MedicalRecordService medicalRecordService;

    @GetMapping("/medicalRecord")
    public List<MedicalRecord> getAllMedicalrecords() {
        return medicalRecordService.findAllMedicalRecords();
    }

    @GetMapping("/medicalRecord/{theFirstName}-{theLastName}")
    public MedicalRecord getMedicalrecord(@PathVariable String theFirstName, @PathVariable String theLastName) {
        return medicalRecordService.findMedicalrecordByFirstNameAndLastName(theFirstName, theLastName);
    }

    @PostMapping("/medicalRecord")
    public void addMedicalrecord(@RequestBody MedicalRecord theMedicalrecord) {
        medicalRecordService.addMedicalrecord(theMedicalrecord);
    }

    @PutMapping("/medicalRecord")
    public void updateMedicalrecord(@RequestBody MedicalRecord theMedicalrecord) {
        medicalRecordService.updateMedicalrecord(theMedicalrecord);
    }

    @DeleteMapping("/medicalRecord/{theFirstName}-{theLastName}")
    public void deleteMedicalrecord(@PathVariable String theFirstName, @PathVariable String theLastName) {
        medicalRecordService.deleteMedicalrecord(theFirstName, theLastName);
    }

}
