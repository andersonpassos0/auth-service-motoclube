package com.motoclub.authservice.infrastructure.controller;

import com.motoclub.authservice.application.dto.AuthRequest;
import com.motoclub.authservice.application.dto.AuthResponse;
import com.motoclub.authservice.application.dto.AuthUserResponse;
import com.motoclub.authservice.application.dto.RegisterRequest;
import com.motoclub.authservice.application.usecase.LoginUserUseCase;
import com.motoclub.authservice.application.usecase.RegisterUserUseCase;
import com.motoclub.authservice.infrastructure.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints públicos para login e registro de novos usuários")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUseruseCase;
    private final JwtService jwtService;

    @PostMapping("/register")
    @Operation(summary = "Registrar novo usuário",
               description = "Cria uma nova conta de usuãrio e retorna um token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request){
        AuthResponse response = registerUserUseCase.execute(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuário",
               description = "Autentica um usuário e retorna token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest){
        AuthResponse response = loginUseruseCase.execute(authRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/validate")
    @Operation(summary = "Validar Token",
               description = "Verifica se o token passado é válido")
    public ResponseEntity<AuthUserResponse> validateToken(@RequestHeader("Authorization") String tokenHeader){

        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")){
            return ResponseEntity.status(401).build();
        }

        String token = tokenHeader.substring(7);

        if (!jwtService.isTokenValid(token)){
            return ResponseEntity.status(401).build();
        }

        String username = jwtService.extractUsername(token);
        List<String> roles = jwtService.extractRoles(token);

        AuthUserResponse response = new AuthUserResponse(username, roles);

        return  ResponseEntity.ok(response);


    }

}
