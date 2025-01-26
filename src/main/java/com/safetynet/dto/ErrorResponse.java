package com.safetynet.dto;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class ErrorResponse {

    private LocalDateTime timestamp;
    private int status;
    private String message;

    public ErrorResponse(int status, String message) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
    }

}
