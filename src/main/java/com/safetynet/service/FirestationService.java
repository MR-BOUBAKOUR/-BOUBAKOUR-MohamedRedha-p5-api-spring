package com.safetynet.service;

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

    public FirestationService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
        this.firestations = dataRepository.getFirestations();
    }

    public List<Firestation> findAllFirestations() {
        logger.info("Finding the firestations from the database");
        return firestations;
    }

    public Firestation findFirestationByAddress(String theAddress) {
        for (Firestation firestation : firestations) {
            if (firestation.getAddress().equals(theAddress)) {
                logger.info("Found the firestation with the address {}", firestation.getAddress());
                return firestation;
            }
        }
        return null;
    }

    public void addFirestation(Firestation theFirestation) {
        firestations.add(theFirestation);
        dataRepository.writeData("firestations", firestations);
        logger.info("{} added successfully", theFirestation.getAddress());
    }

    public void updateFirestation(Firestation theFirestation) {
        boolean found = false;
        for (Firestation firestation : firestations) {
            if (firestation.getAddress().equals(theFirestation.getAddress())) {
                int index = firestations.indexOf(firestation);
                firestations.set(index, theFirestation);
                dataRepository.writeData("firestations", firestations);
                logger.info("{} updated successfully", theFirestation.getAddress());
                found = true;
            }
        }
        if (!found) logger.error("{} not found", theFirestation.getAddress());
    }

    public void deleteFirestation(String theAddress) {
        firestations.removeIf(firestation -> firestation.getAddress().equals(theAddress));
        dataRepository.writeData("firestations", firestations);
        logger.info("{} deleted successfully", theAddress);
    }

}
