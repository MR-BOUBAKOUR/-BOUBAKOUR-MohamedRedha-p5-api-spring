package com.safetynet.dto.medicalrecord;

import lombok.*;

import java.util.List;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MedicalRecordUpdateDTO {

    private String birthdate;
    private List<String> medications;
    private List<String> allergies;

}
