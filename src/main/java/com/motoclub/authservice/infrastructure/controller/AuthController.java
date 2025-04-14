package com.motoclub.authservice.infrastructure.controller;

import com.motoclub.authservice.application.dto.AuthResponse;
import com.motoclub.authservice.application.dto.RegisterRequest;
import com.motoclub.authservice.application.usecase.RegisterUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request){
        AuthResponse response = registerUserUseCase.execute(request);
        return ResponseEntity.ok(response);
    }

}
