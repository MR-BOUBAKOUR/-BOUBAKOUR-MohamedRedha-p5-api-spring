package com.safetynet.dto.person;

import lombok.*;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonForFirestationCoverageResponseDTO {
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
}