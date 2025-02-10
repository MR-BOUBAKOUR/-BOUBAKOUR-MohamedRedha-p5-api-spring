package com.safetynet.service;

import com.safetynet.dto.person.*;
import com.safetynet.dto.search.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The type Search service.
 */
@Service
public class SearchService {

    private static final Logger logger = LoggerFactory.getLogger(SearchService.class);
    private final PersonMapper personMapper;

    /**
     * The Persons.
     */
    List<Person> persons;
    /**
     * The Firestations.
     */
    List<Firestation> firestations;
    /**
     * The Medical records.
     */
    List<MedicalRecord> medicalRecords;

    /**
     * Instantiates a new Search service.
     *
     * @param dataRepository the data repository
     * @param personMapper   the person mapper
     */
    public SearchService(DataRepository dataRepository, PersonMapper personMapper) {
        this.firestations = dataRepository.getFirestations();
        this.medicalRecords = dataRepository.getMedicalRecords();
        this.persons = dataRepository.getPersons();

        this.personMapper = personMapper;
    }

    /**
     * Gets covered persons by station.
     *
     * @param stationNumber the station number
     * @return the covered persons by station
     */
    public FirestationCoverageResponseDTO getCoveredPersonsByStation(int stationNumber) {

        AtomicInteger adultCount = new AtomicInteger();
        AtomicInteger childCount = new AtomicInteger();

        List<Firestation> firestationsWithSameNumberStation = firestations.stream()
            .filter(firestation -> firestation.getStation() == stationNumber)
            .toList();

        if (firestationsWithSameNumberStation.isEmpty()) {
            logger.warn("Resource not found");
            throw new ResourceNotFoundException("No firestations found for station number: " + stationNumber);
        }

        List<PersonForFirestationCoverageResponseDTO> coveredPersons = persons.stream()
            .filter(person ->
                firestationsWithSameNumberStation.stream()
                    .anyMatch(firestation -> firestation.getAddress().equals(person.getAddress())))
            .map(person -> {

                if (getAge(person) >= 18) {
                    adultCount.incrementAndGet();
                } else {
                    childCount.incrementAndGet();
                }

                return personMapper.toPersonForFirestationCoverageResponseDTO(person);
            })
            .toList();

        return new FirestationCoverageResponseDTO(
            adultCount,
            childCount,
            coveredPersons
        );
    }

    /**
     * Gets children by address.
     *
     * @param address the address
     * @return the children by address
     */
    public ChildAlertResponseDTO getChildrenByAddress(String address) {

        List<Person> residents = persons.stream()
            .filter(person -> person.getAddress().equals(address))
            .toList();

        if (residents.isEmpty()) {
            logger.warn("Resource not found");
            return new ChildAlertResponseDTO(List.of());
        }

        List<PersonForChildAlertResponseDTO> children = residents.stream()
            .filter(resident -> getAge(resident) <= 18)
            .map(child -> {

                List<PersonResponseDTO> relatives = residents.stream()
                    .filter(resident -> !resident.equals(child))
                    .map(personMapper::toResponseDTO)
                    .toList();

                return new PersonForChildAlertResponseDTO(
                    child.getFirstName(),
                    child.getLastName(),
                    getAge(child),
                    relatives
                );
            })
            .toList();

        return new ChildAlertResponseDTO(children);
    }

    /**
     * Gets phones by station.
     *
     * @param stationNumber the station number
     * @return the phones by station
     */
    public PhoneAlertResponseDTO getPhonesByStation(int stationNumber) {

        List<Firestation> firestationsWithSameNumberStation = firestations.stream()
            .filter(firestation -> firestation.getStation() == stationNumber)
            .toList();

        if (firestationsWithSameNumberStation.isEmpty()) {
            logger.warn("Resource not found");
            throw new ResourceNotFoundException("No firestations found for station number: " + stationNumber);
        }

        List<String> phones = persons.stream()
            .filter(person ->
                firestationsWithSameNumberStation.stream()
                    .anyMatch(firestation -> firestation.getAddress().equals(person.getAddress())))
            .map(Person::getPhone)
            .toList();

        return new PhoneAlertResponseDTO(phones);
    }

    /**
     * Gets persons by address station.
     *
     * @param address the address
     * @return the persons by address station
     */
    public FireResponseDTO getPersonsByAddressStation(String address) {

        List<Integer> stations = firestations.stream()
            .filter(firestation -> firestation.getAddress().equals(address))
            .map(Firestation::getStation)
            .toList();

        if (stations.isEmpty()) {
            logger.warn("Resource not found");
            throw new ResourceNotFoundException("No firestation found for address: " + address);
        }

        List<Person> residents = persons.stream()
            .filter(person -> person.getAddress().equals(address))
            .toList();

        if (residents.isEmpty()) {
            logger.warn("Resource not found");
            throw new ResourceNotFoundException("No residents found for the address: " + address);
        }

        List<PersonForFireResponseDTO> persons = residents.stream()
            .map(person -> new PersonForFireResponseDTO(
                person.getLastName(),
                person.getPhone(),
                getAge(person),
                getMedications(person),
                getAllergies(person)
            ))
            .toList();

        return new FireResponseDTO(
            stations,
            persons
        );
    }

    /**
     * Gets persons by stations with medical record.
     *
     * @param stationNumbers the station numbers
     * @return the persons by stations with medical record
     */
    public FloodStationsResponseDTO getPersonsByStationsWithMedicalRecord(List<Integer> stationNumbers) {

        List<String> firestationsByAddress = new ArrayList<>();

        for (Integer stationNumber : stationNumbers) {
            firestations.stream()
                .filter(firestation -> firestation.getStation() == stationNumber)
                .map(Firestation::getAddress)
                .forEach(firestationsByAddress::add);
        }

        if (firestationsByAddress.isEmpty()) {
            logger.warn("Resource not found");
            throw new ResourceNotFoundException("Resource not found for station numbers: " + stationNumbers);
        }

        List<Person> residents = persons.stream()
            .filter(person -> firestationsByAddress.contains(person.getAddress()))
            .toList();

        if (residents.isEmpty()) {
            logger.warn("Resource not found");
            throw new ResourceNotFoundException("No residents found for the given stations.");
        }

        List<PersonForFloodStationsResponseDTO> persons = residents.stream()
            .map(person -> new PersonForFloodStationsResponseDTO(
                person.getAddress(),
                person.getLastName(),
                person.getPhone(),
                getAge(person),
                getMedications(person),
                getAllergies(person)
            ))
            .toList();

        return new FloodStationsResponseDTO(persons);
    }

    /**
     * Gets person by last name with medical record.
     *
     * @param lastName the last name
     * @return the person by last name with medical record
     */
    public PersonsInfoLastNameResponseDTO getPersonByLastNameWithMedicalRecord(String lastName) {

        List<PersonForPersonsInfoLastNameResponseDTO> personsTargeted = persons.stream()
                .filter(person -> person.getLastName().equals(lastName))
                .map(person -> new PersonForPersonsInfoLastNameResponseDTO(
                        person.getLastName(),
                        person.getAddress(),
                        getAge(person),
                        person.getEmail(),
                        getMedications(person),
                        getAllergies(person)
                ))
                .toList();

        if (personsTargeted.isEmpty()) {
            logger.warn("Resource not found");
            throw new ResourceNotFoundException("Resource not found for the lastName: " + lastName);
        }

        return new PersonsInfoLastNameResponseDTO(personsTargeted);
    }

    /**
     * Gets emails by city.
     *
     * @param city the city
     * @return the emails by city
     */
    public CommunityEmailResponseDTO getEmailsByCity(String city) {

        List<String> emails = persons.stream()
            .filter(person -> person.getCity().equals(city))
            .map(Person::getEmail)
            .toList();

        if (emails.isEmpty()) {
            logger.warn("Resource not found");
            throw new ResourceNotFoundException("Resource not found for the city: " + city);
        }

        return new CommunityEmailResponseDTO(emails);
    }

    private MedicalRecord getMedicalRecord(Person person) {
        return medicalRecords.stream()
            .filter(record ->
                record.getFirstName().equals(person.getFirstName()) &&
                record.getLastName().equals(person.getLastName()))
            .findFirst()
            .orElseThrow(() -> {
                logger.warn("Resource not found");
                return new ResourceNotFoundException("Medical record not found");
            });
    }

    /**
     * Gets medications.
     *
     * @param person the person
     * @return the medications
     */
    public List<String> getMedications(Person person) {
        MedicalRecord record = getMedicalRecord(person);
        return record.getMedications();
    }

    /**
     * Gets allergies.
     *
     * @param person the person
     * @return the allergies
     */
    public List<String> getAllergies(Person person) {
        MedicalRecord record = getMedicalRecord(person);
        return record.getAllergies();
    }

    /**
     * Gets age.
     *
     * @param person the person
     * @return the age
     */
    public int getAge(Person person) {
        MedicalRecord record = getMedicalRecord(person);
        LocalDate dateOfBirth = LocalDate.parse(record.getBirthdate(), DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }
}
