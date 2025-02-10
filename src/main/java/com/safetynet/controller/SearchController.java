package com.safetynet.controller;

import com.safetynet.dto.search.*;
import com.safetynet.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * The type Search controller.
 */
@Controller
@ResponseBody
public class SearchController {

    @Autowired
    private SearchService searchService;

    /**
     * Gets covered persons by station.
     *
     * @param stationNumber the station number
     * @return the covered persons by station
     */
    @GetMapping("/firestationCoverage")
    public FirestationCoverageResponseDTO getCoveredPersonsByStation(@RequestParam int stationNumber) {
        return searchService.getCoveredPersonsByStation(stationNumber);
    }

    /**
     * Gets children by address.
     *
     * @param address the address
     * @return the children by address
     */
    @GetMapping("/childAlert")
    public ChildAlertResponseDTO getChildrenByAddress(@RequestParam String address) {
        return searchService.getChildrenByAddress(address);
    }

    /**
     * Gets phones by station.
     *
     * @param stationNumber the station number
     * @return the phones by station
     */
    @GetMapping("/phoneAlert")
    public PhoneAlertResponseDTO getPhonesByStation(@RequestParam int stationNumber) {
        return searchService.getPhonesByStation(stationNumber);
    }

    /**
     * Gets persons by address station.
     *
     * @param address the address
     * @return the persons by address station
     */
    @GetMapping("/fire")
    public FireResponseDTO getPersonsByAddressStation(@RequestParam String address) {
        return searchService.getPersonsByAddressStation(address);
    }

    /**
     * Gets persons by stations with medical record.
     *
     * @param stationNumbers the station numbers
     * @return the persons by stations with medical record
     */
    @GetMapping("/flood/stations")
    public FloodStationsResponseDTO getPersonsByStationsWithMedicalRecord(@RequestParam List<Integer> stationNumbers) {
        return searchService.getPersonsByStationsWithMedicalRecord(stationNumbers);
    }

    /**
     * Gets person by last name with medical record.
     *
     * @param lastName the last name
     * @return the person by last name with medical record
     */
    @GetMapping("/personInfoLastName={lastName}")
    public PersonsInfoLastNameResponseDTO getPersonByLastNameWithMedicalRecord(@PathVariable String lastName) {
        return searchService.getPersonByLastNameWithMedicalRecord(lastName);
    }

    /**
     * Gets emails by city.
     *
     * @param city the city
     * @return the emails by city
     */
    @GetMapping("/communityEmail")
    public CommunityEmailResponseDTO getEmailsByCity(@RequestParam String city) {
        return searchService.getEmailsByCity(city);
    }

}
