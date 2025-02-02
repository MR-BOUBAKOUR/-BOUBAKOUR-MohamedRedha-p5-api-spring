package com.safetynet.model;

import lombok.*;

import java.util.List;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class MedicalRecord {

    private String firstName;
    private String lastName;
    private String birthdate;
    private List<String> medications;
    private List<String> allergies;

}
