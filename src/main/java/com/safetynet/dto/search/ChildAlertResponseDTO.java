package com.safetynet.dto.search;

import com.safetynet.dto.person.PersonResponseDTO;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChildAlertResponseDTO {

    private String firstName;
    private String lastName;
    private int age;

    private List<PersonResponseDTO> relatives;

}
