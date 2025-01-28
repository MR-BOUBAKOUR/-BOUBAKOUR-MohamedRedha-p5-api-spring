package com.safetynet.service;

import com.safetynet.dto.person.PersonsByStationCoverageResponseDTO;
import com.safetynet.mapper.PersonMapper;
import com.safetynet.model.Firestation;
import com.safetynet.model.MedicalRecord;
import com.safetynet.model.Person;
import com.safetynet.repository.DataRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class SearchService {

    private final PersonMapper personMapper;
    private final DataRepository dataRepository;

    List<Person> persons;
    List<Firestation> firestations;
    List<MedicalRecord> medicalRecords;

    public SearchService(DataRepository dataRepository, PersonMapper personMapper) {
        this.dataRepository = dataRepository;
        this.firestations = dataRepository.getFirestations();
        this.medicalRecords = dataRepository.getMedicalRecords();
        this.persons = dataRepository.getPersons();

        this.personMapper = personMapper;
    }

    public List<PersonsByStationCoverageResponseDTO> getCoveredPersonsByStation(int stationNumber) {

        int adultCount = 0;
        int childCount = 0;

        List<Firestation> firestationsWithSameNumberStation = firestations.stream()
            .filter(firestation -> firestation.getStation() == stationNumber)
            .toList();

        List<PersonsByStationCoverageResponseDTO> coveredPersons = persons.stream()
            .filter(person -> firestationsWithSameNumberStation.stream()
                    .anyMatch(firestation -> firestation.getAddress().equals(person.getAddress())))
            .map(person -> {

                LocalDate dateOfBirth= getDateOfBirthFromMedicalRecords(person);

                if (isAdult(dateOfBirth)) {
                    adultCount++;
                } else {
                    childCount++;
                }

                return personMapper.toPersonsByStationCoverageResponseDTO(person);
            })
            .toList();

        return coveredPersons;
    }

    public LocalDate getDateOfBirthFromMedicalRecords(Person person) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return medicalRecords.stream()
            .filter(record -> record.getFirstName().equals(person.getFirstName()) &&
                    record.getLastName().equals(person.getLastName()))
            .map(record -> {
                try {
                    return LocalDate.parse(record.getBirthdate(), formatter);
                } catch (DateTimeParseException e) {
                    return null;
                }
            })
            .findFirst()
            .orElse(null);
    }

    private boolean isAdult(LocalDate dateOfBirth) {
        int age = Period.between(dateOfBirth, LocalDate.now()).getYears();
        return age >= 18;
    }

}
