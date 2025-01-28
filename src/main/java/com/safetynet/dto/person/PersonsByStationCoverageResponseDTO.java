package com.safetynet.dto.person;

import lombok.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonsByStationCoverageResponseDTO {

    private AtomicInteger adultCount;
    private AtomicInteger childCount;

    private List<PersonByStationCoverageResponseDTO> persons;

}