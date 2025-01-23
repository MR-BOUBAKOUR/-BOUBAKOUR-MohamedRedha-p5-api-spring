package com.safetynet.controller;

import com.safetynet.model.Person;
import com.safetynet.service.DataAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PersonRestController {

    @Autowired
    private DataAccessService dataAccessService;

    @GetMapping("/person")
    public List<Person> getPersons() {
        return dataAccessService.findPersons();
    }

    @GetMapping("/person/{firstName}-{lastName}")
    public Person getPerson(@PathVariable String firstName, @PathVariable String lastName) {
        return dataAccessService.findPersonByFirstNameAndLastName(firstName, lastName);
    }
}
