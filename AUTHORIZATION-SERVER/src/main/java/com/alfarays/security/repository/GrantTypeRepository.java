package com.alfarays.security.repository;

import com.alfarays.security.entity.GrantType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GrantTypeRepository extends JpaRepository<GrantType, Long> {

    Optional<GrantType> findByGrantType(String grantType);

}