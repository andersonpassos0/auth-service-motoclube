package com.motoclub.authservice.infrastructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.motoclub.authservice.application.dto.AuthRequest;
import com.motoclub.authservice.application.dto.AuthResponse;
import com.motoclub.authservice.application.dto.RegisterRequest;
import com.motoclub.authservice.application.usecase.LoginUserUseCase;
import com.motoclub.authservice.application.usecase.RegisterUserUseCase;
import com.motoclub.authservice.infrastructure.handler.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private RegisterUserUseCase registerUserUseCase;
    @Mock
    private LoginUserUseCase loginUserUseCase;

    @InjectMocks
    private AuthController authController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders
                .standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldRegisterUserSuccessfully() throws Exception{
        RegisterRequest request = new RegisterRequest("Anderson", "anderson@email.com", "123456");
        AuthResponse response = new AuthResponse("fake-jwt-token");

        when(registerUserUseCase.execute(any(RegisterRequest.class))).thenReturn(response);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt-token"));
    }

    @Test
    void shouldLoginUserSuccessfully() throws Exception {
        AuthRequest request = new AuthRequest("anderson@email.com", "123456");
        AuthResponse response = new AuthResponse("fake-jwt-token");

        when(loginUserUseCase.execute(any(AuthRequest.class))).thenReturn(response);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt-token"));
    }

    @Test
    void shouldReturnBadRequestWhenEmailAlreadyExists() throws Exception {
        RegisterRequest request = new RegisterRequest("Anderson", "anderson@email.com", "1234556");

        when(registerUserUseCase.execute(any(RegisterRequest.class))).thenThrow(new IllegalArgumentException("Email já está em uso"));

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Email já está em uso"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.path").value("/auth/register"));


    }

    @Test
    void shouldReturnBadRequestWhenCredentialsAreInvalid() throws Exception {
        AuthRequest request = new AuthRequest("anderson@email.com", "123456");

        when(loginUserUseCase.execute(any(AuthRequest.class))).thenThrow(new IllegalArgumentException("Credenciais inválidas"));

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Credenciais inválidas"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.path").value("/auth/login"));
    }
}
