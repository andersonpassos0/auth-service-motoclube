package com.motoclub.authservice.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta de autenticação trazendo token JWT")
public record AuthResponse (

        @Schema(description = "Token JWT gerado para o usuário")
        String token
){}
