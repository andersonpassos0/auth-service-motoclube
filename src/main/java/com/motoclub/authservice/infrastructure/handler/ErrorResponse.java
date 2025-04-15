package com.motoclub.authservice.infrastructure.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponse {
    private String message;
    private int status;
    private LocalDateTime timestamp;
    private String path;

    public ErrorResponse(String message, int status,String path){
        this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now();
        this.path = path;
    }
}
