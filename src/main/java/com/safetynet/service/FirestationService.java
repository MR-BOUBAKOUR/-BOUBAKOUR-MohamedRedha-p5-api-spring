package com.safetynet.service;

import com.safetynet.dto.firestation.FirestationCreateDTO;
import com.safetynet.dto.firestation.FirestationResponseDTO;
import com.safetynet.dto.firestation.FirestationUpdateDTO;
import com.safetynet.mapper.FirestationMapper;
import com.safetynet.model.Firestation;
import com.safetynet.repository.DataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        logger.info("Finding the firestations from the database");
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
        return null;
    }

    public void addFirestation(FirestationCreateDTO theFirestation) {
        Firestation firestation = firestationMapper.toEntityFromCreateDTO(theFirestation);
        firestations.add(firestation);
        dataRepository.writeData("firestations", firestations);
        logger.info("{} added successfully", theFirestation.getAddress());
    }

    public void updateFirestation(FirestationUpdateDTO theFirestation, String theAddress) {
        boolean found = false;
        for (Firestation firestation : firestations) {
            if (firestation.getAddress().equals(theAddress)) {

                Firestation updatedFirestation = firestationMapper.toEntityFromUpdateDTO(theFirestation);
                updatedFirestation.setAddress(theAddress);

                int index = firestations.indexOf(firestation);
                firestations.set(index, updatedFirestation);
                dataRepository.writeData("firestations", firestations);
                logger.info("{} updated successfully", firestation.getAddress());
                found = true;
            }
        }
        if (!found) logger.error("{} not found", theAddress);
    }

    public void deleteFirestation(String theAddress) {
        firestations.removeIf(firestation -> firestation.getAddress().equals(theAddress));
        dataRepository.writeData("firestations", firestations);
        logger.info("{} deleted successfully", theAddress);
    }

}
