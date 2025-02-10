package com.safetynet.service;

import com.safetynet.dto.person.PersonCreateDTO;
import com.safetynet.dto.person.PersonResponseDTO;
import com.safetynet.dto.person.PersonUpdateDTO;
import com.safetynet.exception.ConflictException;
import com.safetynet.exception.ResourceNotFoundException;
import com.safetynet.mapper.PersonMapper;
import com.safetynet.model.Person;
import com.safetynet.repository.DataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Person service.
 */
@Service
public class PersonService {

    private static final Logger logger = LoggerFactory.getLogger(PersonService.class);

    private final DataRepository dataRepository;
    private final PersonMapper personMapper;

    private final List<Person> persons;

    /**
     * Instantiates a new Person service.
     *
     * @param dataRepository the data repository
     * @param personMapper   the person mapper
     */
    public PersonService(DataRepository dataRepository, PersonMapper personMapper) {
        this.dataRepository = dataRepository;
        this.personMapper = personMapper;
        this.persons = dataRepository.getPersons();
    }

    /**
     * Find all persons list.
     *
     * @return the list
     */
    public List<PersonResponseDTO> findAllPersons() {

        if (persons.isEmpty()) {
            logger.warn("No persons found in the database");
            throw new ResourceNotFoundException("Resource not found");
        }

        return persons
                .stream()
                .map(personMapper::toResponseDTO)
                .toList();
    }

    /**
     * Find person by first name and last name person response dto.
     *
     * @param theFirstName the first name
     * @param theLastName  the last name
     * @return the person response dto
     */
    public PersonResponseDTO findPersonByFirstNameAndLastName(String theFirstName, String theLastName) {
        for (Person person : persons) {
            if (person.getFirstName().equals(theFirstName) && person.getLastName().equals(theLastName)) {
                logger.info("Found the person with the first name {} and last name {}", person.getFirstName(), person.getLastName());
                return personMapper.toResponseDTO(person);
            }
        }

        logger.error("Person not found : {} {}", theFirstName, theLastName);
        throw new ResourceNotFoundException("Resource not found");
    }

    /**
     * Add person.
     *
     * @param thePerson the the person
     */
    public void addPerson(PersonCreateDTO thePerson) {

        boolean exists = persons.stream().anyMatch(persons ->
                persons.getFirstName().equals(thePerson.getFirstName()) && persons.getLastName().equals(thePerson.getLastName())
        );

        if (exists) {
            logger.warn("Person already exist : {} {}", thePerson.getFirstName(), thePerson.getLastName());
            throw new ConflictException("Resource already exist");
        }

        Person person = personMapper.toEntityFromCreateDTO(thePerson);
        persons.add(person);
        dataRepository.writeData("persons", persons);
        logger.info("{} added successfully", person.getFirstName());
    }

    /**
     * Update person.
     *
     * @param thePerson    the person
     * @param theFirstName the first name
     * @param theLastName  the last name
     */
    public void updatePerson(PersonUpdateDTO thePerson, String theFirstName, String theLastName) {

        for (Person person : persons) {
            if (person.getFirstName().equals(theFirstName) &&
                    person.getLastName().equals(theLastName)) {

                // Merging the DTO with the first and last name injected as params
                Person updatedPerson = personMapper.toEntityFromUpdateDTO(thePerson);
                updatedPerson.setFirstName(theFirstName);
                updatedPerson.setLastName(theLastName);

                if (person.equals(updatedPerson)) {
                    logger.warn("The person with the name {} is exactly the same as the one you are trying to update", theLastName);
                    throw new ConflictException("The person with this name is exactly the same as the one you are trying to update");
                }

                // Replace the existing person with the updatedPerson
                int index = persons.indexOf(person);
                persons.set(index, updatedPerson);

                dataRepository.writeData("persons", persons);
                logger.info("{} updated successfully", person.getFirstName());

                return;
            }
        }

        logger.error("Person not found for : {} {}", theFirstName, theLastName);
        throw new ResourceNotFoundException("Resource not found");
    }

    /**
     * Delete person.
     *
     * @param theFirstName the first name
     * @param theLastName  the last name
     */
    public void deletePerson(String theFirstName, String theLastName) {
        // Not using the "persons.remove(person)" to avoid ConcurrentModificationException.
        // Occurs when a collection is modified while it is being iterated over.
        boolean removed = persons.removeIf(person -> person.getFirstName().equals(theFirstName) && person.getLastName().equals(theLastName));

        if (!removed) {
            logger.error("Person not found : {} {}", theFirstName, theLastName);
            throw new ResourceNotFoundException("Resource not found");
        }

        dataRepository.writeData("persons", persons);
        logger.info("{} deleted successfully", theFirstName);
    }

}
