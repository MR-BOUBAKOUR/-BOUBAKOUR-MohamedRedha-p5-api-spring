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

/**
 * The type Person rest controller.
 */
@RestController
public class PersonRestController {

    private final PersonService personService;

    /**
     * Instantiates a new Person rest controller.
     *
     * @param personService the person service
     */
    @Autowired
    public PersonRestController(PersonService personService) {
        this.personService = personService;
    }

    /**
     * Gets all persons.
     *
     * @return the all persons
     */
    @GetMapping("/person")
    public List<PersonResponseDTO> getAllPersons() {
        return personService.findAllPersons();
    }

    /**
     * Gets person.
     *
     * @param theFirstName the first name
     * @param theLastName  the last name
     * @return the person
     */
    @GetMapping("/person/{theFirstName}-{theLastName}")
    public PersonResponseDTO getPerson(@PathVariable String theFirstName, @PathVariable String theLastName) {
        return personService.findPersonByFirstNameAndLastName(theFirstName, theLastName);
    }

    /**
     * Add person response entity.
     *
     * @param thePerson the person
     * @return the response entity
     */
    @PostMapping("/person")
    public ResponseEntity<SuccessResponse> addPerson(@Valid @RequestBody PersonCreateDTO thePerson) {
        personService.addPerson(thePerson);
        SuccessResponse response = new SuccessResponse(HttpStatus.CREATED.value(), "Person added successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Update person response entity.
     *
     * @param thePerson    the person
     * @param theFirstName the first name
     * @param theLastName  the last name
     * @return the response entity
     */
    @PutMapping("/person/{theFirstName}-{theLastName}")
    public ResponseEntity<SuccessResponse> updatePerson(@Valid @RequestBody PersonUpdateDTO thePerson, @PathVariable String theFirstName, @PathVariable String theLastName) {
        personService.updatePerson(thePerson, theFirstName, theLastName);
        SuccessResponse response = new SuccessResponse(HttpStatus.OK.value(), "Person updated successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Delete person response entity.
     *
     * @param theFirstName the first name
     * @param theLastName  the last name
     * @return the response entity
     */
    @DeleteMapping("/person/{theFirstName}-{theLastName}")
    public ResponseEntity<SuccessResponse> deletePerson(@PathVariable String theFirstName, @PathVariable String theLastName) {
        personService.deletePerson(theFirstName, theLastName);
        SuccessResponse response = new SuccessResponse(HttpStatus.OK.value(), "Person deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}