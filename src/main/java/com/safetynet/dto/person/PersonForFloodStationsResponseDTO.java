package com.safetynet.dto.person;

import lombok.*;

import java.util.List;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonForFloodStationsResponseDTO {

    private String address;
    private String lastName;
    private String phone;
    private int age;

    private List<String> medications;
    private List<String> allergies;

}
