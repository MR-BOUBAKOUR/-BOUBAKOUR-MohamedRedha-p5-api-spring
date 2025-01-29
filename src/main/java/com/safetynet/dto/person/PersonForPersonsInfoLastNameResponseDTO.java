package com.safetynet.dto.person;

import lombok.*;

import java.util.List;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonForPersonsInfoLastNameResponseDTO {

    private String lastName;
    private String address;
    private int age;
    private String email;

    private List<String> medications;
    private List<String> allergies;

}
