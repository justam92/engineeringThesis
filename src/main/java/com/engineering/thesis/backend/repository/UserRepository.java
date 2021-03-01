package com.engineering.thesis.backend.repository;

import com.engineering.thesis.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmailAndIsActive(String email, boolean isActive);
    Optional<User> findAllByEmail(String email);
    Optional<User> findByEmail(String email);
    List<User> findAllByIsActiveTrueAndIsEmailVerifiedTrue();
    long countByRoleAndMedicalFacilityId(String role, Long id);
}