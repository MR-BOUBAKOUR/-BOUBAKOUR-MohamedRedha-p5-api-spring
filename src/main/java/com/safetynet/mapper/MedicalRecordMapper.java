package com.safetynet.mapper;

import com.safetynet.dto.medicalrecord.MedicalRecordCreateDTO;
import com.safetynet.dto.medicalrecord.MedicalRecordResponseDTO;
import com.safetynet.dto.medicalrecord.MedicalRecordUpdateDTO;
import com.safetynet.model.MedicalRecord;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MedicalRecordMapper {

    MedicalRecord toEntityFromCreateDTO(MedicalRecordCreateDTO medicalRecordCreateDTO);

    MedicalRecord toEntityFromUpdateDTO(MedicalRecordUpdateDTO medicalRecordUpdateDTO);

    MedicalRecordResponseDTO toResponseDTO(MedicalRecord medicalRecord);

}
