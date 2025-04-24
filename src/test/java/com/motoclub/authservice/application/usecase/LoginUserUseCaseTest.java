package com.motoclub.authservice.application.usecase;

import com.motoclub.authservice.application.dto.AuthRequest;
import com.motoclub.authservice.application.dto.AuthResponse;
import com.motoclub.authservice.domain.model.User;
import com.motoclub.authservice.infrastructure.repository.UserRepository;
import com.motoclub.authservice.infrastructure.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoginUserUseCaseTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    private LoginUserUseCase loginUserUseCase;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        loginUserUseCase = new LoginUserUseCase(userRepository, jwtService, passwordEncoder);
    }

    @Test
    void shouldLoginUserAndReturnToken(){
        AuthRequest request = new AuthRequest("teste@email.com", "123456");
        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.password(), user.getPassword())).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("generatedToken");

        AuthResponse response = loginUserUseCase.execute(request);

        assertNotNull(response);
        assertNotNull(response.token());

        verify(userRepository).findByEmail(request.email());
        verify(passwordEncoder).matches(request.password(), user.getPassword());
        verify(jwtService).generateToken(user);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound(){
        AuthRequest request = new AuthRequest("invalid@email", "123456");

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());

        try{
            loginUserUseCase.execute(request);
        } catch (RuntimeException e) {
            assertNotNull(e);
            assert e.getMessage().equals("Usuário não encontrado");
        }

        verify(userRepository).findByEmail(request.email());
    }

    @Test
    void shouldThrowExceptionWhenPasswordDoesNotMatch(){
        AuthRequest request = new AuthRequest("teste@email.com", "123456");
        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode("wrongPassword"))
                .build();

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.password(), user.getPassword())).thenReturn(false);

        try {
            loginUserUseCase.execute(request);
        } catch (RuntimeException e) {
            assertNotNull(e);
            assert e.getMessage().equals("Senha incorreta");
        }

        verify(userRepository).findByEmail(request.email());
        verify(passwordEncoder).matches(request.password(), user.getPassword());
    }
}