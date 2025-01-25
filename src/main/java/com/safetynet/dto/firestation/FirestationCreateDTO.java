package com.safetynet.dto.firestation;

import lombok.*;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FirestationCreateDTO {

    private String address;
    private int station;

}
