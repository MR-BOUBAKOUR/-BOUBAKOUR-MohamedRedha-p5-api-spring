package com.safetynet.controller;

import com.safetynet.dto.firestation.FirestationCreateDTO;
import com.safetynet.dto.firestation.FirestationResponseDTO;
import com.safetynet.dto.firestation.FirestationUpdateDTO;
import com.safetynet.service.FirestationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FirestationRestController {

    @Autowired
    private FirestationService firestationService;

    @GetMapping("/firestation")
    public List<FirestationResponseDTO> getAllFirestations() {
        return firestationService.findAllFirestations();
    }

    @GetMapping("/firestation/{theAddress}")
    public FirestationResponseDTO getFirestation(@PathVariable String theAddress) {
        return firestationService.findFirestationByAddress(theAddress);
    }

    @PostMapping("/firestation")
    public void addFirestation(@Valid @RequestBody FirestationCreateDTO theFirestation) {
        firestationService.addFirestation(theFirestation);
    }

    @PutMapping("/firestation/{theAddress}")
    public void updateFirestation(@Valid @RequestBody FirestationUpdateDTO theFirestation, @PathVariable String theAddress) {
        firestationService.updateFirestation(theFirestation, theAddress);
    }

    @DeleteMapping("/firestation/{theAddress}")
    public void deleteFirestation(@PathVariable String theAddress) {
        firestationService.deleteFirestation(theAddress);
    }
}
