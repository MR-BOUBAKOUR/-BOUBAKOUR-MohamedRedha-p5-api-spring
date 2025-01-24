package com.safetynet.controller;

import com.safetynet.model.Firestation;
import com.safetynet.service.FirestationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FirestationRestController {

    @Autowired
    private FirestationService firestationService;

    @GetMapping("/firestation")
    public List<Firestation> getAllFirestations() {
        return firestationService.findAllFirestations();
    }

    @GetMapping("/firestation/{theAddress}")
    public Firestation getFirestation(@PathVariable String theAddress) {
        return firestationService.findFirestationByAddress(theAddress);
    }

    @PostMapping("/firestation")
    public void addFirestation(@RequestBody Firestation theFirestation) {
        firestationService.addFirestation(theFirestation);
    }

    @PutMapping("/firestation")
    public void updateFirestation(@RequestBody Firestation theFirestation) {
        firestationService.updateFirestation(theFirestation);
    }

    @DeleteMapping("/firestation/{address}")
    public void deleteFirestation(@PathVariable String address) {
        firestationService.deleteFirestation(address);
    }
}
