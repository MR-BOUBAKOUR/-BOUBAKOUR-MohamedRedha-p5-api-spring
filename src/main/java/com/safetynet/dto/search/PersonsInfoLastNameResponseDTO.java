package com.safetynet.dto.search;

import com.safetynet.dto.person.PersonForPersonsInfoLastNameResponseDTO;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonsInfoLastNameResponseDTO {

    List<PersonForPersonsInfoLastNameResponseDTO> persons;

}
