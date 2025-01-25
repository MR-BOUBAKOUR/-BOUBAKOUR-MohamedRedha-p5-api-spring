package com.safetynet.mapper;

import com.safetynet.dto.person.PersonCreateDTO;
import com.safetynet.dto.person.PersonResponseDTO;
import com.safetynet.dto.person.PersonUpdateDTO;
import com.safetynet.model.Person;
import com.safetynet.model.Person;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    PersonCreateDTO toCreateDTO(Person person);

    Person toEntityFromCreateDTO(PersonCreateDTO personCreateDTO);

    PersonUpdateDTO toUpdateDTO(Person person);

    Person toEntityFromUpdateDTO(PersonUpdateDTO personUpdateDTO);

    PersonResponseDTO toResponseDTO(Person person);

    Person toEntityFromResponseDTO(PersonResponseDTO personResponseDTO);

}
