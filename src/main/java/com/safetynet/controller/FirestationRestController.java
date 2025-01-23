package com.safetynet.controller;

import com.safetynet.model.Firestation;
import com.safetynet.service.DataAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FirestationRestController {

    @Autowired
    private DataAccessService dataAccessService;

    @GetMapping("/firestation")
    public List<Firestation> getFirestation() {
        return dataAccessService.findFirestations();
    }
}
