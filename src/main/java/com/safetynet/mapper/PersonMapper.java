package com.safetynet.mapper;

import com.safetynet.dto.person.*;
import com.safetynet.model.Person;
import org.mapstruct.Mapper;

/**
 * The interface Person mapper.
 */
@Mapper(componentModel = "spring")
public interface PersonMapper {

    /**
     * To entity from create dto person.
     *
     * @param personCreateDTO the person create dto
     * @return the person
     */
    Person toEntityFromCreateDTO(PersonCreateDTO personCreateDTO);

    /**
     * To entity from update dto person.
     *
     * @param personUpdateDTO the person update dto
     * @return the person
     */
    Person toEntityFromUpdateDTO(PersonUpdateDTO personUpdateDTO);

    /**
     * To response dto person response dto.
     *
     * @param person the person
     * @return the person response dto
     */
    PersonResponseDTO toResponseDTO(Person person);

    /**
     * To person for firestation coverage response dto person for firestation coverage response dto.
     *
     * @param person the person
     * @return the person for firestation coverage response dto
     */
    PersonForFirestationCoverageResponseDTO toPersonForFirestationCoverageResponseDTO(Person person);

}
