package com.alfarays.security.repository;

import com.alfarays.security.entity.AuthenticationMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthenticationMethodRepository extends JpaRepository<AuthenticationMethod, Long> {
    Optional<AuthenticationMethod> findByAuthenticationMethod(String authenticationMethod);
}