package com.safetynet.dto.search;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PhoneAlertResponseDTO {

    private List<String> phones;

}
