package com.safetynet.integration;

import com.safetynet.repository.DataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SearchIT {

    @Autowired
    protected MockMvc mockMvc;

    @BeforeEach
    void setUp() throws Exception {
        Path testFile = Path.of("src/test/resources/data-test.json");
        Path tempFile = Path.of("src/test/resources/data-test-temp.json");

        Files.copy(testFile, tempFile, StandardCopyOption.REPLACE_EXISTING);

        DataRepository dataRepository = new DataRepository();
        DataRepository.FILE_PATH = tempFile.toString();

        dataRepository.init();
    }

    @Test
    public void getCoveredPersonsByStation_test() throws Exception {
        mockMvc.perform(get("/firestationCoverage")
                        .param("stationNumber", "1"))
                .andExpect(status().isOk());
    }

    @Test
    public void getChildrenByAddress_test() throws Exception {
        mockMvc.perform(get("/childAlert")
                        .param("address", "892 Downing Ct"))
                .andExpect(status().isOk());
    }

    @Test
    public void getPhonesByStation_test() throws Exception {
        mockMvc.perform(get("/phoneAlert")
                        .param("stationNumber", "1"))
                .andExpect(status().isOk());
    }

    @Test
    public void getPersonsByAddressStation_test() throws Exception {
        mockMvc.perform(get("/fire")
                        .param("address", "748 Townings Dr"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stations[0]").value(3));
    }

    @Test
    public void getPersonsByStationsWithMedicalRecord_test() throws Exception {
        mockMvc.perform(get("/flood/stations")
                        .param("stationNumbers", "1"))
                .andExpect(status().isOk());
    }

    @Test
    public void getPersonByLastNameWithMedicalRecord_test() throws Exception {
        mockMvc.perform(get("/personInfoLastName=Boyd"))
                .andExpect(status().isOk());
    }

    @Test
    public void getEmailsByCity_test() throws Exception {
        mockMvc.perform(get("/communityEmail")
                        .param("city", "Culver"))
                .andExpect(status().isOk());
    }
}
