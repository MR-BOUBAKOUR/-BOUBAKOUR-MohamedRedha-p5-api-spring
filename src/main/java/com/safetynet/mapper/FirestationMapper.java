package com.safetynet.mapper;

import com.safetynet.dto.firestation.FirestationCreateDTO;
import com.safetynet.dto.firestation.FirestationResponseDTO;
import com.safetynet.dto.firestation.FirestationUpdateDTO;
import com.safetynet.model.Firestation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FirestationMapper {

    Firestation toEntityFromCreateDTO(FirestationCreateDTO firestationCreateDTO);

    Firestation toEntityFromUpdateDTO(FirestationUpdateDTO firestationUpdateDTO);

    FirestationResponseDTO toResponseDTO(Firestation firestation);

}
