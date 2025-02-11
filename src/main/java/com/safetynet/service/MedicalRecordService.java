package com.safetynet.service;

import com.safetynet.dto.medicalrecord.MedicalRecordCreateDTO;
import com.safetynet.dto.medicalrecord.MedicalRecordResponseDTO;
import com.safetynet.dto.medicalrecord.MedicalRecordUpdateDTO;
import com.safetynet.exception.ConflictException;
import com.safetynet.exception.ResourceNotFoundException;
import com.safetynet.mapper.MedicalRecordMapper;
import com.safetynet.model.MedicalRecord;
import com.safetynet.repository.DataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Medical record service.
 */
@Service
public class MedicalRecordService {

    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordService.class);

    private final DataRepository dataRepository;
    private final MedicalRecordMapper medicalRecordMapper;

    private final List<MedicalRecord> medicalRecords;

    /**
     * Instantiates a new Medical record service.
     *
     * @param dataRepository      the data repository
     * @param medicalRecordMapper the medical record mapper
     */
    public MedicalRecordService(DataRepository dataRepository, MedicalRecordMapper medicalRecordMapper) {
        this.dataRepository = dataRepository;
        this.medicalRecordMapper = medicalRecordMapper;

        this.medicalRecords = dataRepository.getMedicalRecords();
    }

    /**
     * Find all medical records list.
     *
     * @return the list
     */
    public List<MedicalRecordResponseDTO> findAllMedicalRecords() {

        if (medicalRecords.isEmpty()) {
            logger.warn("No medicalRecords found in the database");
            throw new ResourceNotFoundException("Resource not found");
        }

        return medicalRecords
                .stream()
                .map(medicalRecordMapper::toResponseDTO)
                .toList();
    }

    /**
     * Find medicalrecord by first name and last name medical record response dto.
     *
     * @param theFirstName the first name
     * @param theLastName  the last name
     * @return the medical record response dto
     */
    public MedicalRecordResponseDTO findMedicalrecordByFirstNameAndLastName(String theFirstName, String theLastName) {

        for (MedicalRecord medicalRecord : medicalRecords) {
            if (medicalRecord.getFirstName().equals(theFirstName) && medicalRecord.getLastName().equals(theLastName)) {
                logger.info("Found the medicalRecord with the first name {} and last name {}", medicalRecord.getFirstName(), medicalRecord.getLastName());
                return medicalRecordMapper.toResponseDTO(medicalRecord);
            }
        }

        logger.error("MedicalRecord not found for : {} {}", theFirstName, theLastName);
        throw new ResourceNotFoundException("Resource not found");
    }

    /**
     * Add medicalrecord.
     *
     * @param theMedicalrecord the the medicalrecord
     */
    public void addMedicalrecord(MedicalRecordCreateDTO theMedicalrecord) {

        boolean exists = medicalRecords.stream().anyMatch(medicalRecord ->
                medicalRecord.getFirstName().equals(theMedicalrecord.getFirstName()) && medicalRecord.getLastName().equals(theMedicalrecord.getLastName())
        );

        if (exists) {
            logger.warn("Medicalrecord already exists at for : {} {}", theMedicalrecord.getFirstName(), theMedicalrecord.getLastName());
            throw new ConflictException("Resource already exist");
        }

        MedicalRecord medicalRecord = medicalRecordMapper.toEntityFromCreateDTO(theMedicalrecord);
        medicalRecords.add(medicalRecord);
        dataRepository.writeData("medicalrecords", medicalRecords);
        logger.info("{} added successfully", theMedicalrecord.getFirstName());
    }

    /**
     * Update medicalrecord.
     *
     * @param theMedicalrecord the medicalrecord
     * @param theFirstName     the first name
     * @param theLastName      the last name
     */
    public void updateMedicalrecord(MedicalRecordUpdateDTO theMedicalrecord, String theFirstName, String theLastName) {

        for (MedicalRecord medicalRecord : medicalRecords) {
            if (medicalRecord.getFirstName().equals(theFirstName) &&
                    medicalRecord.getLastName().equals(theLastName)) {

                MedicalRecord updatedMedicalRecord = medicalRecordMapper.toEntityFromUpdateDTO(theMedicalrecord);
                updatedMedicalRecord.setFirstName(theFirstName);
                updatedMedicalRecord.setLastName(theLastName);

                if (medicalRecord.equals(updatedMedicalRecord)) {
                    logger.warn("The medicalRecord with the name {} is exactly the same as the one you are trying to update", theLastName);
                    throw new ConflictException("The medicalRecord with this name is exactly the same as the one you are trying to update");
                }

                int index = medicalRecords.indexOf(medicalRecord);
                medicalRecords.set(index, updatedMedicalRecord);
                dataRepository.writeData("medicalRecords", medicalRecords);
                logger.info("{} updated successfully", theFirstName + " " + theLastName);

                return;
            }
        }

        logger.error("MedicalRecord not found for : {} {}", theFirstName, theLastName);
        throw new ResourceNotFoundException("Resource not found");
    }

    /**
     * Delete medicalrecord.
     *
     * @param theFirstName the first name
     * @param theLastName  the last name
     */
    public void deleteMedicalrecord(String theFirstName, String theLastName) {
        boolean removed = medicalRecords.removeIf(medicalRecord -> medicalRecord.getFirstName().equals(theFirstName) && medicalRecord.getLastName().equals(theLastName));

        if (!removed) {
            logger.error("MedicalRecord not found for address: {} {}", theFirstName, theLastName);
            throw new ResourceNotFoundException("Resource not found");
        }

        dataRepository.writeData("medicalRecord", medicalRecords);
        logger.info("{} deleted successfully", theFirstName);
    }

}
