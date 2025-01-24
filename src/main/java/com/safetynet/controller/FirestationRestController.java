package com.safetynet.controller;

import com.safetynet.model.Firestation;
import com.safetynet.service.DataAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FirestationRestController {

    @Autowired
    private DataAccessService dataAccessService;

    @GetMapping("/firestation")
    public List<Firestation> getAllFirestations() {
        return dataAccessService.findAllFirestations();
    }

    @GetMapping("/firestation/{theAddress}")
    public Firestation getFirestation(@PathVariable String theAddress) {
        return dataAccessService.findFirestationByAddress(theAddress);
    }

    @PostMapping("/firestation")
    public void addFirestation(@RequestBody Firestation theFirestation) {
        dataAccessService.addFirestation(theFirestation);
    }

    @PutMapping("/firestation")
    public void updateFirestation(@RequestBody Firestation theFirestation) {
        dataAccessService.updateFirestation(theFirestation);
    }

    @DeleteMapping("/firestation/{address}")
    public void deleteFirestation(@PathVariable String address) {
        dataAccessService.deleteFirestation(address);
    }
}
