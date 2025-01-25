package com.safetynet.controller;

import com.safetynet.dto.person.PersonCreateDTO;
import com.safetynet.dto.person.PersonResponseDTO;
import com.safetynet.dto.person.PersonUpdateDTO;
import com.safetynet.service.PersonService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PersonRestController {

    private static final Logger logger = LoggerFactory.getLogger(PersonRestController.class);

    @Autowired
    private PersonService personService;

    @GetMapping("/person")
    public List<PersonResponseDTO> getAllPersons() {
        return personService.findAllPersons();
    }

    @GetMapping("/person/{theFirstName}-{theLastName}")
    public PersonResponseDTO getPerson(@PathVariable String theFirstName, @PathVariable String theLastName) {
        return personService.findPersonByFirstNameAndLastName(theFirstName, theLastName);
    }

    @PostMapping("/person")
    public void addPerson(@Valid @RequestBody PersonCreateDTO thePerson) {
        personService.addPerson(thePerson);
    }

    @PutMapping("/person/{theFirstName}-{theLastName}")
    public void updatePerson(@Valid @RequestBody PersonUpdateDTO thePerson, @PathVariable String theFirstName, @PathVariable String theLastName) {
        personService.updatePerson(thePerson, theFirstName, theLastName);
    }

    @DeleteMapping("/person/{theFirstName}-{theLastName}")
    public void deletePerson(@PathVariable String theFirstName, @PathVariable String theLastName) {
        personService.deletePerson(theFirstName, theLastName);
    }
}
