package com.safetynet.controller;

import com.safetynet.model.MedicalRecord;
import com.safetynet.service.DataAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MedicalRecordsRestController {

    @Autowired
    DataAccessService dataAccessService;

    @GetMapping("/medicalrecord")
    public List<MedicalRecord> medicalrecords() {
        return dataAccessService.findMedicalRecords();
    }

}
