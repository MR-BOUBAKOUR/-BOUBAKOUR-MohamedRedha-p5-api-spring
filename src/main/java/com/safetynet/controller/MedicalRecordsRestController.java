package com.safetynet.controller;

import com.safetynet.model.MedicalRecord;
import com.safetynet.service.DataAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MedicalRecordsRestController {

    @Autowired
    DataAccessService dataAccessService;

    @GetMapping("/medicalRecord")
    public List<MedicalRecord> getAllMedicalrecords() {
        return dataAccessService.findAllMedicalRecords();
    }

    @GetMapping("/medicalRecord/{theFirstName}-{theLastName}")
    public MedicalRecord getMedicalrecord(@PathVariable String theFirstName, @PathVariable String theLastName) {
        return dataAccessService.findMedicalrecordByFirstNameAndLastName(theFirstName, theLastName);
    }

    @PostMapping("/medicalRecord")
    public void addMedicalrecord(@RequestBody MedicalRecord theMedicalrecord) {
        dataAccessService.addMedicalrecord(theMedicalrecord);
    }

    @PutMapping("/medicalRecord")
    public void updateMedicalrecord(@RequestBody MedicalRecord theMedicalrecord) {
        dataAccessService.updateMedicalrecord(theMedicalrecord);
    }

    @DeleteMapping("/medicalRecord/{theFirstName}-{theLastName}")
    public void deleteMedicalrecord(@PathVariable String theFirstName, @PathVariable String theLastName) {
        dataAccessService.deleteMedicalrecord(theFirstName, theLastName);
    }

}
