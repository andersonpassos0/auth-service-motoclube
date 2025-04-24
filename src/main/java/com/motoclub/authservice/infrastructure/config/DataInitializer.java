package com.motoclub.authservice.infrastructure.config;

import com.motoclub.authservice.domain.model.User;
import com.motoclub.authservice.infrastructure.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, BCryptPasswordEncoder encoder){
        return args -> {
            if (userRepository.findAll().isEmpty()){
                userRepository.save(new User(null, "Admin", "admin@motoclub.com", encoder.encode("123456")));
            }
        };
    }
}
