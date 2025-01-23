package com.safetynet.controller;

import com.safetynet.model.Person;
import com.safetynet.service.DataAccessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PersonRestController {

    private static final Logger logger = LoggerFactory.getLogger(PersonRestController.class);

    @Autowired
    private DataAccessService dataAccessService;

    @GetMapping("/person")
    public List<Person> getPersons() {
        return dataAccessService.findPersons();
    }

    @GetMapping("/person/{theFirstName}-{theLastName}")
    public Person getPerson(@PathVariable String theFirstName, @PathVariable String theLastName) {
        return dataAccessService.findPersonByFirstNameAndLastName(theFirstName, theLastName);
    }

    @PostMapping("/person")
    public void addPerson(@RequestBody Person thePerson) {
        dataAccessService.addPerson(thePerson);
    }

    @PutMapping("/person")
    public void updatePerson(@RequestBody Person thePerson) {
        dataAccessService.updatePerson(thePerson);
    }

    @DeleteMapping("/person/{theFirstName}-{theLastName}")
    public void deletePerson(@PathVariable String theFirstName, @PathVariable String theLastName) {
        dataAccessService.deletePerson(theFirstName, theLastName);
    }
}
