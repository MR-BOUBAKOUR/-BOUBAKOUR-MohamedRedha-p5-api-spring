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

/**
 * The type Firestation rest controller.
 */
@RestController
public class FirestationRestController {

    private final FirestationService firestationService;

    /**
     * Instantiates a new Firestation rest controller.
     *
     * @param firestationService the firestation service
     */
    @Autowired
    public FirestationRestController(FirestationService firestationService) {
        this.firestationService = firestationService;
    }

    /**
     * Gets all firestations.
     *
     * @return the all firestations
     */
    @GetMapping("/firestation")
    public List<FirestationResponseDTO> getAllFirestations() {
        return firestationService.findAllFirestations();
    }

    /**
     * Gets firestation.
     *
     * @param theAddress the address
     * @return the firestation
     */
    @GetMapping("/firestation/{theAddress}")
    public FirestationResponseDTO getFirestation(@PathVariable String theAddress) {
        return firestationService.findFirestationByAddress(theAddress);
    }

    /**
     * Add firestation response entity.
     *
     * @param theFirestation the firestation
     * @return the response entity
     */
    @PostMapping("/firestation")
    public ResponseEntity<SuccessResponse> addFirestation(@Valid @RequestBody FirestationCreateDTO theFirestation) {
        firestationService.addFirestation(theFirestation);
        SuccessResponse response = new SuccessResponse(HttpStatus.CREATED.value(), "Firestation added successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Update firestation response entity.
     *
     * @param theFirestation the firestation
     * @param theAddress     the address
     * @return the response entity
     */
    @PutMapping("/firestation/{theAddress}")
    public ResponseEntity<SuccessResponse> updateFirestation(@Valid @RequestBody FirestationUpdateDTO theFirestation, @PathVariable String theAddress) {
        firestationService.updateFirestation(theFirestation, theAddress);
        SuccessResponse response = new SuccessResponse(HttpStatus.OK.value(), "Firestation updated successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Delete firestation response entity.
     *
     * @param theAddress the address
     * @return the response entity
     */
    @DeleteMapping("/firestation/{theAddress}")
    public ResponseEntity<SuccessResponse> deleteFirestation(@PathVariable String theAddress) {
        firestationService.deleteFirestation(theAddress);
        SuccessResponse response = new SuccessResponse(HttpStatus.OK.value(), "Firestation deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
