package com.safetynet.dto.person;

import jakarta.validation.constraints.*;
import lombok.*;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonUpdateDTO {

    @NotEmpty(message = "Address cannot be empty")
    private String address;

    @NotEmpty(message = "City cannot be empty")
    private String city;

    @NotEmpty(message = "Zip cannot be empty")
    @Pattern(regexp = "^\\d{5}$", message = "Zip code must be exactly 5 digits")
    private String zip;

    @NotEmpty(message = "Phone cannot be empty")
    @Pattern(regexp = "^\\d{3}-\\d{3}-\\d{4}$", message = "Phone number must follow the format xxx-xxx-xxxx")
    private String phone;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    private String email;

}