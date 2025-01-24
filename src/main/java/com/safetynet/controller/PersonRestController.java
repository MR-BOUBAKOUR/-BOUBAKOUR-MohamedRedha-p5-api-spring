package com.safetynet.controller;

import com.safetynet.model.Person;
import com.safetynet.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PersonRestController {

    @Autowired
    private PersonService personService;

    @GetMapping("/person")
    public List<Person> getAllPersons() {
        return personService.findAllPersons();
    }

    @GetMapping("/person/{theFirstName}-{theLastName}")
    public Person getPerson(@PathVariable String theFirstName, @PathVariable String theLastName) {
        return personService.findPersonByFirstNameAndLastName(theFirstName, theLastName);
    }

    @PostMapping("/person")
    public void addPerson(@RequestBody Person thePerson) {
        personService.addPerson(thePerson);
    }

    @PutMapping("/person")
    public void updatePerson(@RequestBody Person thePerson) {
        personService.updatePerson(thePerson);
    }

    @DeleteMapping("/person/{theFirstName}-{theLastName}")
    public void deletePerson(@PathVariable String theFirstName, @PathVariable String theLastName) {
        personService.deletePerson(theFirstName, theLastName);
    }
}
