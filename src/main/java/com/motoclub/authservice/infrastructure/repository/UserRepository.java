package com.motoclub.authservice.infrastructure.repository;

import com.motoclub.authservice.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
