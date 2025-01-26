package com.safetynet.dto.firestation;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FirestationUpdateDTO {

    @NotNull(message = "Station number cannot be null")
    @Min(value = 1, message = "Station number must be greater than 0")
    @Max(value = 999, message = "Station number must be less than 1000")
    private int station;

}
