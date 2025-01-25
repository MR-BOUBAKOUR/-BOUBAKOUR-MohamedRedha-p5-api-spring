package com.safetynet.dto.person;

import lombok.*;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonUpdateDTO {

    private String address;
    private String city;
    private long zip;
    private String phone;
    private String email;

}