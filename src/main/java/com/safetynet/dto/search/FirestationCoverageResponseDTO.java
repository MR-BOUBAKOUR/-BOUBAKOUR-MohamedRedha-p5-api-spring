package com.safetynet.dto.search;

import com.safetynet.dto.person.PersonForFirestationCoverageResponseDTO;
import lombok.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FirestationCoverageResponseDTO {

    private AtomicInteger adultCount;
    private AtomicInteger childCount;

    private List<PersonForFirestationCoverageResponseDTO> persons;

}