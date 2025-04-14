package com.motoclub.authservice.application.usecase;

import com.motoclub.authservice.application.dto.AuthResponse;
import com.motoclub.authservice.application.dto.RegisterRequest;
import com.motoclub.authservice.domain.model.User;
import com.motoclub.authservice.infrastructure.repository.UserRepository;
import com.motoclub.authservice.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterUserUseCase {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthResponse execute(RegisterRequest request){
        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();

        userRepository.save(user);
        String token = jwtService.generateToken(user);

        return new AuthResponse(token);
    }
}
