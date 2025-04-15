package com.motoclub.authservice.application.service;

import com.motoclub.authservice.application.dto.AuthRequest;
import com.motoclub.authservice.application.dto.AuthResponse;
import com.motoclub.authservice.domain.model.User;
import com.motoclub.authservice.infrastructure.repository.UserRepository;
import com.motoclub.authservice.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginuserUseCase {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthResponse execute (AuthRequest authRequest){
        User user = userRepository.findByEmail(authRequest.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(authRequest.password(), user.getPassword())){
            throw new RuntimeException("Senha incorreta");
        }

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }
}
