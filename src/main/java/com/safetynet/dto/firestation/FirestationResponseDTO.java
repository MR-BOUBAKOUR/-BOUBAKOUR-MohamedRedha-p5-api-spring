package com.safetynet.dto.firestation;

import jakarta.validation.constraints.*;
import lombok.*;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FirestationResponseDTO {

    @NotEmpty(message = "Address cannot be empty")
    @Size(min = 5, max = 255, message = "Address must be between 5 and 255 characters")
    private String address;

    @NotNull(message = "Station number cannot be null")
    @Min(value = 1, message = "Station number must be greater than 0")
    @Max(value = 999, message = "Station number must be less than 1000")
    private int station;

}
