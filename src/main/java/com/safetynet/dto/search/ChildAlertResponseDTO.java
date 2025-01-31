package com.safetynet.dto.search;

import com.safetynet.dto.person.PersonForChildAlertResponseDTO;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChildAlertResponseDTO {

    List<PersonForChildAlertResponseDTO> children;

}
