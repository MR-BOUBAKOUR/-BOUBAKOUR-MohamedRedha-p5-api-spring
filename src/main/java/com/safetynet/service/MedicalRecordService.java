package com.safetynet.service;

import com.safetynet.dto.medicalrecord.MedicalRecordCreateDTO;
import com.safetynet.dto.medicalrecord.MedicalRecordResponseDTO;
import com.safetynet.dto.medicalrecord.MedicalRecordUpdateDTO;
import com.safetynet.mapper.MedicalRecordMapper;
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
    private final MedicalRecordMapper medicalRecordMapper;

    public MedicalRecordService(DataRepository dataRepository, MedicalRecordMapper medicalRecordMapper) {
        this.dataRepository = dataRepository;
        this.medicalRecords = dataRepository.getMedicalRecords();
        this.medicalRecordMapper = medicalRecordMapper;
    }

    public List<MedicalRecordResponseDTO> findAllMedicalRecords() {
        logger.info("Finding the medical records from the database");
        return medicalRecords
                .stream()
                .map(medicalRecordMapper::toResponseDTO)
                .toList();
    }

    public MedicalRecordResponseDTO findMedicalrecordByFirstNameAndLastName(String theFirstName, String theLastName) {
        for (MedicalRecord medicalRecord : medicalRecords) {
            if (medicalRecord.getFirstName().equals(theFirstName) && medicalRecord.getLastName().equals(theLastName)) {
                logger.info("Found the medicalRecord with the first name {} and last name {}", medicalRecord.getFirstName(), medicalRecord.getLastName());
                return medicalRecordMapper.toResponseDTO(medicalRecord);
            }
        }
        return null;
    }

    public void addMedicalrecord(MedicalRecordCreateDTO theMedicalrecord) {
        MedicalRecord medicalRecord = medicalRecordMapper.toEntityFromCreateDTO(theMedicalrecord);
        medicalRecords.add(medicalRecord);
        dataRepository.writeData("medicalrecords", medicalRecords);
        logger.info("{} added successfully", theMedicalrecord.getFirstName());
    }

    public void updateMedicalrecord(MedicalRecordUpdateDTO theMedicalrecord, String theFirstName, String theLastName) {
        boolean found = false;
        for (MedicalRecord medicalRecord : medicalRecords) {
            if (medicalRecord.getFirstName().equals(theFirstName) &&
                    medicalRecord.getLastName().equals(theLastName)) {

                MedicalRecord updatedMedicalRecord = medicalRecordMapper.toEntityFromUpdateDTO(theMedicalrecord);
                updatedMedicalRecord.setFirstName(theFirstName);
                updatedMedicalRecord.setLastName(theLastName);

                int index = medicalRecords.indexOf(medicalRecord);
                medicalRecords.set(index, updatedMedicalRecord);
                dataRepository.writeData("medicalRecords", medicalRecords);
                logger.info("{} updated successfully", theFirstName + " " + theLastName);
                found = true;
            }
        }
        if (!found) logger.error("{} not found", theFirstName);
    }

    public void deleteMedicalrecord(String theFirstName, String theLastName) {
        medicalRecords.removeIf(medicalRecord -> medicalRecord.getFirstName().equals(theFirstName) && medicalRecord.getLastName().equals(theLastName));
        dataRepository.writeData("medicalRecord", medicalRecords);
        logger.info("{} deleted successfully", theFirstName);
    }

}
