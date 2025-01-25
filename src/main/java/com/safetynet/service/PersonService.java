package com.safetynet.service;

import com.safetynet.dto.person.PersonCreateDTO;
import com.safetynet.dto.person.PersonResponseDTO;
import com.safetynet.dto.person.PersonUpdateDTO;
import com.safetynet.mapper.PersonMapper;
import com.safetynet.model.Person;
import com.safetynet.repository.DataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    private static final Logger logger = LoggerFactory.getLogger(PersonService.class);

    private final DataRepository dataRepository;
    private final List<Person> persons;
    private final PersonMapper personMapper;

    public PersonService(DataRepository dataRepository, PersonMapper personMapper) {
        this.dataRepository = dataRepository;
        this.persons = dataRepository.getPersons();
        this.personMapper = personMapper;
    }

    public List<PersonResponseDTO> findAllPersons() {
        logger.info("Finding the persons from the database");
        return persons
                .stream()
                .map(personMapper::toResponseDTO)
                .toList();
    }

    public PersonResponseDTO findPersonByFirstNameAndLastName(String theFirstName, String theLastName) {
        for (Person person : persons) {
            if (person.getFirstName().equals(theFirstName) && person.getLastName().equals(theLastName)) {
                logger.info("Found the person with the first name {} and last name {}", person.getFirstName(), person.getLastName());
                return personMapper.toResponseDTO(person);
            }
        }
        return null;
    }

    public void addPerson(PersonCreateDTO thePerson) {
        Person person = personMapper.toEntityFromCreateDTO(thePerson);
        persons.add(person);
        dataRepository.writeData("persons", persons);
        logger.info("{} added successfully", person.getFirstName());
    }

    public void updatePerson(PersonUpdateDTO thePerson, String theFirstName, String theLastName) {
        boolean found = false;
        for (Person person : persons) {
            if (person.getFirstName().equals(theFirstName) &&
                    person.getLastName().equals(theLastName)) {

                // Merging the DTO with the first and last name injected as params
                Person updatedPerson = personMapper.toEntityFromUpdateDTO(thePerson);
                updatedPerson.setFirstName(theFirstName);
                updatedPerson.setLastName(theLastName);

                // Replace the existing person with the updatedPerson
                int index = persons.indexOf(person);
                persons.set(index, updatedPerson);

                dataRepository.writeData("persons", persons);
                logger.info("{} updated successfully", person.getFirstName());
                found = true;
            }
        }
        if (!found) logger.error("{} not found", theFirstName + " " + theLastName);
    }

    public void deletePerson(String theFirstName, String theLastName) {
        // Not using the "persons.remove(person)" to avoid ConcurrentModificationException.
        // Occurs when a collection is modified while it is being iterated over.
        persons.removeIf(person -> person.getFirstName().equals(theFirstName) && person.getLastName().equals(theLastName));
        dataRepository.writeData("persons", persons);
        logger.info("{} deleted successfully", theFirstName);
    }

}
