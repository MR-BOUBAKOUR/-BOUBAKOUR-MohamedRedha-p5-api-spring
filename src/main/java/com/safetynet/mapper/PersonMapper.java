package com.safetynet.mapper;

import com.safetynet.dto.person.*;
import com.safetynet.model.Person;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    Person toEntityFromCreateDTO(PersonCreateDTO personCreateDTO);

    Person toEntityFromUpdateDTO(PersonUpdateDTO personUpdateDTO);

    PersonResponseDTO toResponseDTO(Person person);

    PersonForFirestationCoverageResponseDTO toPersonForFirestationCoverageResponseDTO(Person person);

}
