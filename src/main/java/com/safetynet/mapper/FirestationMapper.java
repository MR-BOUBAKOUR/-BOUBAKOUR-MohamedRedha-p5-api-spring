package com.safetynet.mapper;

import com.safetynet.dto.firestation.FirestationCreateDTO;
import com.safetynet.dto.firestation.FirestationResponseDTO;
import com.safetynet.dto.firestation.FirestationUpdateDTO;
import com.safetynet.model.Firestation;
import org.mapstruct.Mapper;

/**
 * The interface Firestation mapper.
 */
@Mapper(componentModel = "spring")
public interface FirestationMapper {

    /**
     * To entity from create dto firestation.
     *
     * @param firestationCreateDTO the firestation create dto
     * @return the firestation
     */
    Firestation toEntityFromCreateDTO(FirestationCreateDTO firestationCreateDTO);

    /**
     * To entity from update dto firestation.
     *
     * @param firestationUpdateDTO the firestation update dto
     * @return the firestation
     */
    Firestation toEntityFromUpdateDTO(FirestationUpdateDTO firestationUpdateDTO);

    /**
     * To response dto firestation response dto.
     *
     * @param firestation the firestation
     * @return the firestation response dto
     */
    FirestationResponseDTO toResponseDTO(Firestation firestation);

}
