package com.safetynet.service;

import com.safetynet.dto.person.PersonByStationCoverageResponseDTO;
import com.safetynet.dto.person.PersonsByStationCoverageResponseDTO;
import com.safetynet.exception.ResourceNotFoundException;
import com.safetynet.mapper.PersonMapper;
import com.safetynet.model.Firestation;
import com.safetynet.model.MedicalRecord;
import com.safetynet.model.Person;
import com.safetynet.repository.DataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class SearchService {

    private static final Logger logger = LoggerFactory.getLogger(SearchService.class);

    List<Person> persons;
    List<Firestation> firestations;
    List<MedicalRecord> medicalRecords;

    private final PersonMapper personMapper;

    public SearchService(DataRepository dataRepository, PersonMapper personMapper) {
        this.firestations = dataRepository.getFirestations();
        this.medicalRecords = dataRepository.getMedicalRecords();
        this.persons = dataRepository.getPersons();

        this.personMapper = personMapper;
    }

    public PersonsByStationCoverageResponseDTO getCoveredPersonsByStation(int stationNumber) {

        AtomicInteger adultCount = new AtomicInteger();
        AtomicInteger childCount = new AtomicInteger();

        List<Firestation> firestationsWithSameNumberStation = firestations.stream()
                .filter(firestation -> firestation.getStation() == stationNumber)
                .toList();

        if (firestationsWithSameNumberStation.isEmpty()) {
            throw new ResourceNotFoundException("No firestations found for station number: " + stationNumber);
        }

        List<PersonByStationCoverageResponseDTO> coveredPersons = persons.stream()
                .filter(person -> firestationsWithSameNumberStation.stream()
                        .anyMatch(firestation -> firestation.getAddress().equals(person.getAddress())))
                .map(person -> {

                    LocalDate dateOfBirth = getDateOfBirth(person);
                    int age = calculateAge(dateOfBirth);

                    if (age >= 18) {
                        adultCount.incrementAndGet();
                    } else if (age >= 0) {
                        childCount.incrementAndGet();
                    }

                    return personMapper.toPersonByStationCoverageResponseDTO(person);
                })
                .toList();

        return new PersonsByStationCoverageResponseDTO(
                adultCount,
                childCount,
                coveredPersons
        );
    }

    public LocalDate getDateOfBirth(Person person) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return medicalRecords.stream()
            .filter(record ->
                record.getFirstName().equals(person.getFirstName())
                && record.getLastName().equals(person.getLastName()))
            .map(record -> {
                try {
                    return LocalDate.parse(record.getBirthdate(), formatter);
                } catch (DateTimeParseException e) {
                    logger.warn("Date format exception for person: {} {}", person.getFirstName(), person.getLastName());
                    logger.warn(String.valueOf(e));
                    throw new IllegalArgumentException("Invalid date format in medical record");
                }
            })
            .findFirst()
            .orElse(null);
    }

    public int calculateAge(LocalDate dateOfBirth) {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

}
