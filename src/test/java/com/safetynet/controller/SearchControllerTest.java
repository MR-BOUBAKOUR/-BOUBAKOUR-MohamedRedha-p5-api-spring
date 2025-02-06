package com.safetynet.controller;

import com.safetynet.dto.person.PersonForPersonsInfoLastNameResponseDTO;
import com.safetynet.dto.search.*;
import com.safetynet.service.SearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SearchController.class)
public class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SearchService searchService;

    @InjectMocks
    private SearchController searchController;

    @Test
    public void getCoveredPersonsByStation_Test() throws Exception {

        int stationNumber = 1;
        AtomicInteger adultCount = new AtomicInteger(5);
        AtomicInteger childCount = new AtomicInteger(2);

        FirestationCoverageResponseDTO response = new FirestationCoverageResponseDTO(
                adultCount,
                childCount,
                List.of()
        );

        when(searchService.getCoveredPersonsByStation(stationNumber)).thenReturn(response);

        mockMvc.perform(get("/firestationCoverage")
                        .param("stationNumber", String.valueOf(stationNumber)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.adultCount").value(5))
                .andExpect(jsonPath("$.childCount").value(2));
    }

    @Test
    public void getChildrenByAddress_Test() throws Exception {
        String address = "123 Main St";
        ChildAlertResponseDTO response = new ChildAlertResponseDTO(List.of());
        when(searchService.getChildrenByAddress(address)).thenReturn(response);

        mockMvc.perform(get("/childAlert").param("address", address))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.children").isEmpty());
    }

    @Test
    public void getPhonesByStation_Test() throws Exception {

        int stationNumber = 1;
        PhoneAlertResponseDTO response = new PhoneAlertResponseDTO(List.of("123-456-7890"));
        when(searchService.getPhonesByStation(stationNumber)).thenReturn(response);

        mockMvc.perform(get("/phoneAlert").param("stationNumber", String.valueOf(stationNumber)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phones[0]").value("123-456-7890"));
    }

    @Test
    public void getPersonsByAddressStation_Test() throws Exception {

        String address = "123 Main St";
        FireResponseDTO response = new FireResponseDTO(List.of(1), List.of());
        when(searchService.getPersonsByAddressStation(address)).thenReturn(response);

        mockMvc.perform(get("/fire").param("address", address))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stations[0]").value(1));

    }

    @Test
    public void getPersonsByStationsWithMedicalRecord_Test() throws Exception {

        List<Integer> stationNumbers = List.of(1, 2);
        FloodStationsResponseDTO response = new FloodStationsResponseDTO(List.of());
        when(searchService.getPersonsByStationsWithMedicalRecord(stationNumbers)).thenReturn(response);

        mockMvc.perform(get("/flood/stations").param("stationNumbers", "1,2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.persons").isEmpty());
    }

    @Test
    public void getPersonByLastNameWithMedicalRecord_Test() throws Exception {

        PersonsInfoLastNameResponseDTO response = new PersonsInfoLastNameResponseDTO(List.of(
                new PersonForPersonsInfoLastNameResponseDTO(
                        "Smith",
                        "456 Oak St",
                        34,
                        "john.smith@example.com",
                        List.of("Aspirin", "Ibuprofen"),
                        List.of("Peanuts")
                ),
                new PersonForPersonsInfoLastNameResponseDTO(
                        "Johnson",
                        "789 Pine St",
                        29,
                        "emily.johnson@example.com",
                        List.of("Lisinopril"),
                        List.of("Dust", "Pollen")
                )
        ));
        when(searchService.getPersonByLastNameWithMedicalRecord("Smith")).thenReturn(response);

        mockMvc.perform(get("/personInfoLastName={lastName}", "Smith"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.persons[0].lastName").value("Smith"));
    }

    @Test
    public void getEmailsByCity_Test() throws Exception {

        String city = "Springfield";
        CommunityEmailResponseDTO response = new CommunityEmailResponseDTO(List.of("test@example.com"));
        when(searchService.getEmailsByCity(city)).thenReturn(response);

        mockMvc.perform(get("/communityEmail").param("city", city))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.emails[0]").value("test@example.com"));
    }
}
