package com.safetynet.dto.medicalrecord;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MedicalRecordResponseDTO {

    @NotEmpty(message = "First name cannot be empty")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @NotEmpty(message = "Birthdate cannot be empty")
    @Pattern(regexp = "^\\d{2}/\\d{2}/\\d{4}$", message = "Birthdate must be in the format MM/dd/yyyy")
    private String birthdate;

    private List<String> medications;

    private List<String> allergies;

}
