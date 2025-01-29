package com.safetynet.mapper;

import com.safetynet.dto.person.PersonForFireResponseDTO;
import com.safetynet.dto.person.PersonForFirestationCoverageResponseDTO;
import com.safetynet.dto.person.PersonForFloodStationsResponseDTO;
import com.safetynet.dto.person.PersonForPersonsInfoLastNameResponseDTO;
import com.safetynet.model.Person;
import com.safetynet.model.MedicalRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SearchMapper {

    PersonForFirestationCoverageResponseDTO toPersonForFirestationCoverageResponseDTO(Person person);

    @Mapping(source = "person.lastName", target = "lastName")
    @Mapping(source = "person.phone", target = "phone")
    @Mapping(source = "medicalRecord.medications", target = "medications")
    @Mapping(source = "medicalRecord.allergies", target = "allergies")
    PersonForFireResponseDTO toPersonForFireResponseDTO(Person person, MedicalRecord medicalRecord);

    @Mapping(source = "person.address", target = "address")
    @Mapping(source = "person.lastName", target = "lastName")
    @Mapping(source = "person.phone", target = "phone")
    @Mapping(source = "medicalRecord.medications", target = "medications")
    @Mapping(source = "medicalRecord.allergies", target = "allergies")
    PersonForFloodStationsResponseDTO toPersonForFloodStationsResponseDTO(Person person, MedicalRecord medicalRecord);

    @Mapping(source = "person.lastName", target = "lastName")
    @Mapping(source = "person.address", target = "address")
    @Mapping(source = "person.email", target = "email")
    @Mapping(source = "medicalRecord.medications", target = "medications")
    @Mapping(source = "medicalRecord.allergies", target = "allergies")
    PersonForPersonsInfoLastNameResponseDTO toPersonForPersonsInfoLastNameResponseDTO(Person person, MedicalRecord medicalRecord);


}
