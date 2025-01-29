package com.safetynet.service;

import com.safetynet.dto.person.ChildForChildAlertResponseDTO;
import com.safetynet.dto.person.PersonForFirestationCoverageResponseDTO;
import com.safetynet.dto.person.PersonResponseDTO;
import com.safetynet.dto.search.*;
import com.safetynet.exception.ResourceNotFoundException;
import com.safetynet.mapper.PersonMapper;
import com.safetynet.mapper.SearchMapper;
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
    private final PersonMapper personMapper;

    List<Person> persons;
    List<Firestation> firestations;
    List<MedicalRecord> medicalRecords;

    private final SearchMapper searchMapper;

    public SearchService(DataRepository dataRepository, SearchMapper searchMapper, PersonMapper personMapper) {
        this.firestations = dataRepository.getFirestations();
        this.medicalRecords = dataRepository.getMedicalRecords();
        this.persons = dataRepository.getPersons();

        this.searchMapper = searchMapper;
        this.personMapper = personMapper;
    }

    public FirestationCoverageResponseDTO getCoveredPersonsByStation(int stationNumber) {

        AtomicInteger adultCount = new AtomicInteger();
        AtomicInteger childCount = new AtomicInteger();

        List<Firestation> firestationsWithSameNumberStation = firestations.stream()
            .filter(firestation -> firestation.getStation() == stationNumber)
            .toList();

        if (firestationsWithSameNumberStation.isEmpty()) {
            throw new ResourceNotFoundException("No firestations found for station number: " + stationNumber);
        }

        List<PersonForFirestationCoverageResponseDTO> coveredPersons = persons.stream()
            .filter(person ->
                    firestationsWithSameNumberStation.stream().anyMatch(firestation -> firestation.getAddress().equals(person.getAddress())))
            .map(person -> {

                LocalDate dateOfBirth = getDateOfBirth(person);
                int age = calculateAge(dateOfBirth);

                if (age >= 18) {
                    adultCount.incrementAndGet();
                } else {
                    childCount.incrementAndGet();
                }

                return searchMapper.toPersonForFirestationCoverageResponseDTO(person);
            })
            .toList();

        return new FirestationCoverageResponseDTO(
            adultCount,
            childCount,
            coveredPersons
        );
    }

    public ChildAlertResponseDTO getChildrenByAddress(String address) {

        List<Person> residents = persons.stream()
            .filter(person -> person.getAddress().equals(address))
            .toList();

        if (residents.isEmpty()) {
            return new ChildAlertResponseDTO(List.of());
        }

        List<ChildForChildAlertResponseDTO> children = residents.stream()
            .filter(resident -> {
                LocalDate dateOfBirth = getDateOfBirth(resident);
                return calculateAge(dateOfBirth) <= 18;
            })
            .map(child -> {
                LocalDate birthDate = getDateOfBirth(child);
                int age = calculateAge(birthDate);

                List<PersonResponseDTO> relatives = residents.stream()
                    .filter(resident -> !resident.equals(child))
                    .map(personMapper::toResponseDTO)
                    .toList();

                return new ChildForChildAlertResponseDTO(
                    child.getFirstName(),
                    child.getLastName(),
                    age,
                    relatives
                );
            })
            .toList();

        return new ChildAlertResponseDTO(children);
    }

    public PhoneAlertResponseDTO getPhonesByStation(int stationNumber) {

        List<Firestation> firestationsWithSameNumberStation = firestations.stream()
            .filter(firestation -> firestation.getStation() == stationNumber)
            .toList();

        if (firestationsWithSameNumberStation.isEmpty()) {
            throw new ResourceNotFoundException("No firestations found for station number: " + stationNumber);
        }

        List<String> phones = persons.stream()
            .filter(person ->
                    firestationsWithSameNumberStation.stream().anyMatch(firestation -> firestation.getAddress().equals(person.getAddress())))
            .map(Person::getPhone)
            .toList();

        return new PhoneAlertResponseDTO(phones);
    }

    public FireResponseDTO getPersonsByAddressStation(String address) {
        return null;
    }

    public FloodStationsResponseDTO getPersonsByStationsWithMedicalRecord(List<Integer> stationNumber) {
        return null;
    }

    public PersonsInfoLastNameResponseDTO getPersonByLastNameWithMedicalRecord(String lastName) {
        return null;
    }

    public CommunityEmailResponseDTO getEmailsByCity(String city) {

        List<String> emails = persons.stream()
                .filter(person -> person.getCity().equals(city))
                .map(Person::getEmail)
                .toList();

        if (emails.isEmpty()) {
            throw new ResourceNotFoundException("Resource not found for the city: " + city);
        }

        return new CommunityEmailResponseDTO(emails);
    }

    public LocalDate getDateOfBirth(Person person) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
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
