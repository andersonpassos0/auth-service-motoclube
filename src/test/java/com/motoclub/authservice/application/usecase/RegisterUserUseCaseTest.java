package com.motoclub.authservice.application.usecase;

import com.motoclub.authservice.application.dto.AuthResponse;
import com.motoclub.authservice.application.dto.RegisterRequest;
import com.motoclub.authservice.domain.model.User;
import com.motoclub.authservice.infrastructure.repository.UserRepository;
import com.motoclub.authservice.infrastructure.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RegisterUserUseCaseTest {

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private RegisterUserUseCase registerUserUseCase;

    @Test
    void shouldRegisterUserSuccessfully(){
        RegisterRequest request = new RegisterRequest("Anderson", "anderson@email.com", "senha123");

        String encodedPassword = "encodedSenha123";
        String fakeToken = "fake.jwt.token";

        when(passwordEncoder.encode("senha123")).thenReturn(encodedPassword);
        when(jwtService.generateToken(any(User.class))).thenReturn(fakeToken);

        AuthResponse response = registerUserUseCase.execute(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals("Anderson", savedUser.getName());
        assertEquals("anderson@email.com", savedUser.getEmail());
        assertEquals(encodedPassword, savedUser.getPassword());

        assertNotNull(response);
        assertEquals(fakeToken, response.token());
    }

    @Test
    void shouldThrowExceptionWhenSavingUserFails(){
        RegisterRequest request = new RegisterRequest("Anderson", "anderson@email.com", "123456");
        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(request.password())
                .build();

        when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Erro ao salvar usuário"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            registerUserUseCase.execute(request);
        });

        assertEquals("Erro ao salvar usuário", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists(){
        RegisterRequest request = new RegisterRequest("Anderson", "anderson@email.com", "123456");

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(new User()));

        assertThrows(IllegalArgumentException.class, () -> {
            registerUserUseCase.execute(request);
        });
    }
}
