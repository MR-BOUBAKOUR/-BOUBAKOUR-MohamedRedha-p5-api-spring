package com.safetynet.service;

import com.safetynet.model.MedicalRecord;
import com.safetynet.repository.DataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalRecordService {

    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordService.class);

    private final DataRepository dataRepository;
    private final List<MedicalRecord> medicalRecords;

    public MedicalRecordService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
        this.medicalRecords = dataRepository.getMedicalRecords();
    }

    public List<MedicalRecord> findAllMedicalRecords() {
        logger.info("Finding the medical records from the database");
        return medicalRecords;
    }

    public MedicalRecord findMedicalrecordByFirstNameAndLastName(String theFirstName, String theLastName) {
        for (MedicalRecord medicalRecord : medicalRecords) {
            if (medicalRecord.getFirstName().equals(theFirstName) && medicalRecord.getLastName().equals(theLastName)) {
                logger.info("Found the medicalRecord with the first name {} and last name {}", medicalRecord.getFirstName(), medicalRecord.getLastName());
                return medicalRecord;
            }
        }
        return null;
    }

    public void addMedicalrecord(MedicalRecord theMedicalrecord) {
        medicalRecords.add(theMedicalrecord);
        dataRepository.writeData("medicalrecords", medicalRecords);
        logger.info("{} added successfully", theMedicalrecord.getFirstName());
    }

    public void updateMedicalrecord(MedicalRecord theMedicalrecord) {
        boolean found = false;
        for (MedicalRecord medicalRecord : medicalRecords) {
            if (medicalRecord.getFirstName().equals(theMedicalrecord.getFirstName()) &&
                    medicalRecord.getLastName().equals(theMedicalrecord.getLastName())) {
                int index = medicalRecords.indexOf(medicalRecord);
                medicalRecords.set(index, theMedicalrecord);
                dataRepository.writeData("medicalRecords", medicalRecords);
                logger.info("{} updated successfully", theMedicalrecord.getFirstName());
                found = true;
            }
        }
        if (!found) logger.error("{} not found", theMedicalrecord.getFirstName());
    }

    public void deleteMedicalrecord(String theFirstName, String theLastName) {
        medicalRecords.removeIf(medicalRecord -> medicalRecord.getFirstName().equals(theFirstName) && medicalRecord.getLastName().equals(theLastName));
        dataRepository.writeData("medicalRecord", medicalRecords);
        logger.info("{} deleted successfully", theFirstName);
    }

}
