package com.safetynet.model;

import lombok.*;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Person {

    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private long zip;
    private String phone;
    private String email;

}
