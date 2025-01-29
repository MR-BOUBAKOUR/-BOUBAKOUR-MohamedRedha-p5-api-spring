package com.safetynet.dto.search;

import com.safetynet.dto.person.PersonForFireResponseDTO;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FireResponseDTO {

    private List<Integer> stations;

    private List<PersonForFireResponseDTO> persons;

}
