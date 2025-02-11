package com.safetynet.dto.person;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonForChildAlertResponseDTO {

    private String firstName;
    private String lastName;
    private int age;

    private List<PersonResponseDTO> relatives;

}
