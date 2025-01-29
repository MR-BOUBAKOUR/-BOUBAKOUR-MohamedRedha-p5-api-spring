package com.safetynet.dto.search;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommunityEmailResponseDTO {

    private List<String> emails;

}
