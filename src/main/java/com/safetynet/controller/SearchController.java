package com.safetynet.controller;

import com.safetynet.dto.person.PersonsByStationCoverageResponseDTO;
import com.safetynet.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@ResponseBody
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/firestationCoverage")
    public PersonsByStationCoverageResponseDTO getPersonsByFireStationCoverage(@RequestParam int stationNumber) {
        return searchService.getCoveredPersonsByStation(stationNumber);
    }

}
