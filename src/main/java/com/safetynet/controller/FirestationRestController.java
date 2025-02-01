package com.safetynet.controller;

import com.safetynet.dto.SuccessResponse;
import com.safetynet.dto.firestation.FirestationCreateDTO;
import com.safetynet.dto.firestation.FirestationResponseDTO;
import com.safetynet.dto.firestation.FirestationUpdateDTO;
import com.safetynet.service.FirestationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<SuccessResponse> addFirestation(@Valid @RequestBody FirestationCreateDTO theFirestation) {
        firestationService.addFirestation(theFirestation);
        SuccessResponse response = new SuccessResponse(HttpStatus.CREATED.value(), "Firestation added successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/firestation/{theAddress}")
    public ResponseEntity<SuccessResponse> updateFirestation(@Valid @RequestBody FirestationUpdateDTO theFirestation, @PathVariable String theAddress) {
        firestationService.updateFirestation(theFirestation, theAddress);
        SuccessResponse response = new SuccessResponse(HttpStatus.OK.value(), "Firestation updated successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/firestation/{theAddress}")
    public ResponseEntity<SuccessResponse> deleteFirestation(@PathVariable String theAddress) {
        firestationService.deleteFirestation(theAddress);
        SuccessResponse response = new SuccessResponse(HttpStatus.OK.value(), "Firestation deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
