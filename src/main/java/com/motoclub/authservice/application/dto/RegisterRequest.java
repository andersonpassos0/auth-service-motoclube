package com.motoclub.authservice.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Requisição de registro de novo usuário")
public record RegisterRequest (

        @Schema(description = "Nome do usuário")
        @NotBlank
        String name,

        @Schema(description = "E-mail do usuário", example = "usuario@email.com")
        @Email
        String email,

        @Schema(description = "Senha do usuário", example = "minhasenha")
        String password
){}
