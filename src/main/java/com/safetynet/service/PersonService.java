package com.safetynet.service;

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

    public PersonService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
        this.persons = dataRepository.getPersons();
    }

    public List<Person> findAllPersons() {
        logger.info("Finding the persons from the database");
        return dataRepository.getPersons();
    }

    public Person findPersonByFirstNameAndLastName(String theFirstName, String theLastName) {
        for (Person person : persons) {
            if (person.getFirstName().equals(theFirstName) && person.getLastName().equals(theLastName)) {
                logger.info("Found the person with the first name {} and last name {}", person.getFirstName(), person.getLastName());
                return person;
            }
        }
        return null;
    }

    public void addPerson(Person person) {
        persons.add(person);
        dataRepository.writeData("persons", persons);
        logger.info("{} added successfully", person.getFirstName());
    }

    public void updatePerson(Person thePerson) {
        boolean found = false;
        for (Person person : persons) {
            if (person.getFirstName().equals(thePerson.getFirstName()) &&
                    person.getLastName().equals(thePerson.getLastName())) {

                // Replace the existing person with the updated person
                int index = persons.indexOf(person);
                persons.set(index, thePerson);

                dataRepository.writeData("persons", persons);
                logger.info("{} updated successfully", person.getFirstName());
                found = true;
            }
        }
        if (!found) logger.error("{} not found", thePerson.getFirstName());
    }

    public void deletePerson(String theFirstName, String theLastName) {
        // Not using the "persons.remove(person)" to avoid ConcurrentModificationException.
        // Occurs when a collection is modified while it is being iterated over.
        persons.removeIf(person -> person.getFirstName().equals(theFirstName) && person.getLastName().equals(theLastName));
        dataRepository.writeData("persons", persons);
        logger.info("{} deleted successfully", theFirstName);
    }

}
