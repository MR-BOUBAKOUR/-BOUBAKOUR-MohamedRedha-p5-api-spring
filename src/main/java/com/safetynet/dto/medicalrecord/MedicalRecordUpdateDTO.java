package com.safetynet.dto.medicalrecord;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MedicalRecordUpdateDTO {

    @NotEmpty(message = "Birthdate cannot be empty")
    @Pattern(regexp = "^\\d{2}/\\d{2}/\\d{4}$", message = "Birthdate must be in the format MM/dd/yyyy")
    @Past(message = "Birthdate must be a past date")
    private String birthdate;

    private List<String> medications;

    private List<String> allergies;

}
