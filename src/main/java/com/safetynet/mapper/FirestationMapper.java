package com.safetynet.mapper;

import com.safetynet.dto.firestation.FirestationCreateDTO;
import com.safetynet.dto.firestation.FirestationResponseDTO;
import com.safetynet.dto.firestation.FirestationUpdateDTO;
import com.safetynet.model.Firestation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FirestationMapper {

    FirestationCreateDTO toCreateDTO(Firestation firestation);

    Firestation toEntityFromCreateDTO(FirestationCreateDTO firestationCreateDTO);

    FirestationUpdateDTO toUpdateDTO(Firestation firestation);

    Firestation toEntityFromUpdateDTO(FirestationUpdateDTO firestationUpdateDTO);

    FirestationResponseDTO toResponseDTO(Firestation firestation);

    Firestation toEntityFromResponseDTO(FirestationResponseDTO firestationResponseDTO);

}
