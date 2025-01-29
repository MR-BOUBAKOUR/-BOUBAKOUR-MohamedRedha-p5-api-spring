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

@Controller
@ResponseBody
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/firestationCoverage")
    public FirestationCoverageResponseDTO getCoveredPersonsByStation(@RequestParam int stationNumber) {
        return searchService.getCoveredPersonsByStation(stationNumber);
    }

    @GetMapping("/childAlert")
    public ChildAlertResponseDTO getChildrenByAddress(@RequestParam String address) {
        return searchService.getChildrenByAddress(address);
    }

    @GetMapping("/phoneAlert")
    public PhoneAlertResponseDTO getPhonesByStation(@RequestParam int stationNumber) {
        return searchService.getPhonesByStation(stationNumber);
    }

    @GetMapping("/fire")
    public FireResponseDTO getPersonsByAddressStation(@RequestParam String address) {
        return searchService.getPersonsByAddressStation(address);
    }

    @GetMapping("/flood/stations")
    public FloodStationsResponseDTO getPersonsByStationsWithMedicalRecord(@RequestParam List<Integer> stationNumber) {
        return searchService.getPersonsByStationsWithMedicalRecord(stationNumber);
    }

    @GetMapping("/personInfoLastName={lastName}")
    public PersonsInfoLastNameResponseDTO getPersonByLastNameWithMedicalRecord(@PathVariable String lastName) {
        return searchService.getPersonByLastNameWithMedicalRecord(lastName);
    }

    @GetMapping("/communityEmail")
    public CommunityEmailResponseDTO getEmailsByCity(@RequestParam String city) {
        return searchService.getEmailsByCity(city);
    }

}
