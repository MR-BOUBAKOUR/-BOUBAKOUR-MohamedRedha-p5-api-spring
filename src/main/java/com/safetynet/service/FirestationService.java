package com.safetynet.service;

import com.safetynet.dto.firestation.FirestationCreateDTO;
import com.safetynet.dto.firestation.FirestationResponseDTO;
import com.safetynet.dto.firestation.FirestationUpdateDTO;
import com.safetynet.exception.ConflictException;
import com.safetynet.exception.ResourceNotFoundException;
import com.safetynet.mapper.FirestationMapper;
import com.safetynet.model.Firestation;
import com.safetynet.repository.DataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FirestationService {

    private static final Logger logger = LoggerFactory.getLogger(FirestationService.class);

    private final DataRepository dataRepository;
    private final List<Firestation> firestations;
    private final FirestationMapper firestationMapper;

    public FirestationService(DataRepository dataRepository, FirestationMapper firestationMapper) {
        this.dataRepository = dataRepository;
        this.firestations = dataRepository.getFirestations();
        this.firestationMapper = firestationMapper;
    }

    public List<FirestationResponseDTO> findAllFirestations() {

        if (firestations.isEmpty()) {
            logger.warn("No firestations found in the database");
            throw new ResourceNotFoundException("Resource not found");
        }

        return firestations
                .stream()
                .map(firestationMapper::toResponseDTO)
                .toList();
    }

    public FirestationResponseDTO findFirestationByAddress(String theAddress) {

        for (Firestation firestation : firestations) {
            if (firestation.getAddress().equals(theAddress)) {
                logger.info("Found the firestation with the address {}", firestation.getAddress());
                return firestationMapper.toResponseDTO(firestation);
            }
        }

        logger.error("Firestation not found for address: {}", theAddress);
        throw new ResourceNotFoundException("Resource not found");
    }

    /*

    public FirestationResponseDTO findFirestationByAddress(String theAddress) {
        return firestations.stream()
                .filter(firestation -> firestation.getAddress().equals(theAddress))
                .findFirst()
                .map(firestation -> {
                    logger.info("Found firestation at the address: {}", firestation.getAddress());
                    return firestationMapper.toResponseDTO(firestation);
                })
                .orElseThrow(() -> {
                    logger.error("Firestation not found for address: {}", theAddress);
                    return new ResourceNotFoundException("Resource not found");
                });
    }

    */

    public void addFirestation(FirestationCreateDTO theFirestation) {

        boolean exists = firestations.stream()
                .anyMatch(firestation -> firestation.getAddress().equals(theFirestation.getAddress()));

        if (exists) {
            logger.warn("Firestation already exists at the address: {}", theFirestation.getAddress());
            throw new ConflictException("Resource already exist");
        }

        Firestation firestation = firestationMapper.toEntityFromCreateDTO(theFirestation);
        firestations.add(firestation);
        dataRepository.writeData("firestations", firestations);
        logger.info("{} added successfully", theFirestation.getAddress());
    }

    public void updateFirestation(FirestationUpdateDTO theFirestation, String theAddress) {

        for (Firestation firestation : firestations) {
            if (firestation.getAddress().equals(theAddress)) {

                Firestation updatedFirestation = firestationMapper.toEntityFromUpdateDTO(theFirestation);
                updatedFirestation.setAddress(theAddress);

                if (firestation.equals(updatedFirestation)) {
                    logger.warn("The firestation with the address {} is exactly the same as the one you are trying to update", theAddress);
                    throw new ConflictException("The firestation with this address is exactly the same as the one you are trying to update");
                }

                int index = firestations.indexOf(firestation);
                firestations.set(index, updatedFirestation);
                dataRepository.writeData("firestations", firestations);
                logger.info("Firestation at {} updated successfully", theAddress);

                return;
            }
        }

        logger.error("Firestation not found for address: {}", theAddress);
        throw new ResourceNotFoundException("Resource not found");
    }

    public void deleteFirestation(String theAddress) {

        boolean removed = firestations.removeIf(firestation -> firestation.getAddress().equals(theAddress));

        if (!removed) {
            logger.error("Firestation not found for address: {}", theAddress);
            throw new ResourceNotFoundException("Resource not found");
        }

        dataRepository.writeData("firestations", firestations);
        logger.info("{} deleted successfully", theAddress);
    }

}
