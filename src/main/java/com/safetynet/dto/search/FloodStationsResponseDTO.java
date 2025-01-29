package com.safetynet.dto.search;

import com.safetynet.dto.person.PersonForFloodStationsResponseDTO;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FloodStationsResponseDTO {

    List<PersonForFloodStationsResponseDTO> persons;

}
