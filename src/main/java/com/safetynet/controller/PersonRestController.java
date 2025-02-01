package com.safetynet.controller;

import com.safetynet.dto.SuccessResponse;
import com.safetynet.dto.person.PersonCreateDTO;
import com.safetynet.dto.person.PersonResponseDTO;
import com.safetynet.dto.person.PersonUpdateDTO;
import com.safetynet.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PersonRestController {

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
    public ResponseEntity<SuccessResponse> addPerson(@Valid @RequestBody PersonCreateDTO thePerson) {
        personService.addPerson(thePerson);
        SuccessResponse response = new SuccessResponse(HttpStatus.CREATED.value(), "Person added successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/person/{theFirstName}-{theLastName}")
    public ResponseEntity<SuccessResponse> updatePerson(@Valid @RequestBody PersonUpdateDTO thePerson, @PathVariable String theFirstName, @PathVariable String theLastName) {
        personService.updatePerson(thePerson, theFirstName, theLastName);
        SuccessResponse response = new SuccessResponse(HttpStatus.OK.value(), "Person updated successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/person/{theFirstName}-{theLastName}")
    public ResponseEntity<SuccessResponse> deletePerson(@PathVariable String theFirstName, @PathVariable String theLastName) {
        personService.deletePerson(theFirstName, theLastName);
        SuccessResponse response = new SuccessResponse(HttpStatus.OK.value(), "Person deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}