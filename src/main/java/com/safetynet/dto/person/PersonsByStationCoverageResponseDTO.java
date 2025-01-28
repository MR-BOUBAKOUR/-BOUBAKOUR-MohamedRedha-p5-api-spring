package com.safetynet.dto.person;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonsByStationCoverageResponseDTO {

    private String firstName;
    private String lastName;
    private String address;
    private String phone;

    private int adultCount;
    private int childCount;

}
