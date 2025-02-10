package com.safetynet.mapper;

import com.safetynet.dto.medicalrecord.MedicalRecordCreateDTO;
import com.safetynet.dto.medicalrecord.MedicalRecordResponseDTO;
import com.safetynet.dto.medicalrecord.MedicalRecordUpdateDTO;
import com.safetynet.model.MedicalRecord;
import org.mapstruct.Mapper;

/**
 * The interface Medical record mapper.
 */
@Mapper(componentModel = "spring")
public interface MedicalRecordMapper {

    /**
     * To entity from create dto medical record.
     *
     * @param medicalRecordCreateDTO the medical record create dto
     * @return the medical record
     */
    MedicalRecord toEntityFromCreateDTO(MedicalRecordCreateDTO medicalRecordCreateDTO);

    /**
     * To entity from update dto medical record.
     *
     * @param medicalRecordUpdateDTO the medical record update dto
     * @return the medical record
     */
    MedicalRecord toEntityFromUpdateDTO(MedicalRecordUpdateDTO medicalRecordUpdateDTO);

    /**
     * To response dto medical record response dto.
     *
     * @param medicalRecord the medical record
     * @return the medical record response dto
     */
    MedicalRecordResponseDTO toResponseDTO(MedicalRecord medicalRecord);

}
