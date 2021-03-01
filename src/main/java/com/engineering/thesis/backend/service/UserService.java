package com.engineering.thesis.backend.service;

import com.engineering.thesis.backend.exception.ResourceNotFoundException;
import com.engineering.thesis.backend.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getById(Long id) throws ResourceNotFoundException;
    String deactivateByEmail(String email) throws ResourceNotFoundException;
    String update(User user);
    List<User> getAll();
    Optional<User> findByEmail(String email);
    long countByRoleAndMedicalFacilityId(String role, Long medicalFacilityId);
}
