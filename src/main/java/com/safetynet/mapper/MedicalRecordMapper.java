package com.safetynet.mapper;

import com.safetynet.dto.medicalrecord.MedicalRecordCreateDTO;
import com.safetynet.dto.medicalrecord.MedicalRecordResponseDTO;
import com.safetynet.dto.medicalrecord.MedicalRecordUpdateDTO;
import com.safetynet.model.MedicalRecord;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MedicalRecordMapper {

    MedicalRecordCreateDTO toCreateDTO(MedicalRecord medicalRecord);

    MedicalRecord toEntityFromCreateDTO(MedicalRecordCreateDTO medicalRecordCreateDTO);

    MedicalRecordUpdateDTO toUpdateDTO(MedicalRecord medicalRecord);

    MedicalRecord toEntityFromUpdateDTO(MedicalRecordUpdateDTO medicalRecordUpdateDTO);

    MedicalRecordResponseDTO toResponseDTO(MedicalRecord medicalRecord);

    MedicalRecord toEntityFromResponseDTO(MedicalRecordResponseDTO medicalRecordResponseDTO);

}
