package com.safetynet.dto.person;

import lombok.*;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonCreateDTO {

    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private long zip;
    private String phone;
    private String email;

}