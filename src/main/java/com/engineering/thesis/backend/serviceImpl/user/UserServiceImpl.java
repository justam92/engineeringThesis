package com.engineering.thesis.backend.serviceImpl.user;

import com.engineering.thesis.backend.exception.ResourceNotFoundException;
import com.engineering.thesis.backend.model.User;
import com.engineering.thesis.backend.repository.UserRepository;
import com.engineering.thesis.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> getById(Long id) throws ResourceNotFoundException {
        if (id == null) {
            throw new RuntimeException("Error: Id can't be empty.");
        }
        if (userRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Error: User with id: " + id + " doesn't exist.");
        }
        return userRepository.findById(id);
    }

    @Override
    public String deactivateByEmail(String email) throws ResourceNotFoundException {
        if (email.isBlank()) {
            throw new RuntimeException("Error: Email can't be empty.");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Error: User with this email: " + email + " doesn't exist."));
        user.setActive(false);
        userRepository.save(user);
        return "User with email: " + user.getEmail() + " is untenable from now.";
    }

    @Override
    public String update(User user) {
        if (user == null) {
            throw new RuntimeException("Error: User fields can't be empty.");
        }
        if (userRepository.findByEmail(user.getEmail()).isEmpty()) {
            throw new RuntimeException("Error: User with this email: " + user.getEmail() + " doesn't exist.");
        }
        User userToUpdate = userRepository.findByEmail(user.getEmail()).get();
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setName(user.getName());
        userToUpdate.setSurname(user.getSurname());
        userToUpdate.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(userToUpdate);
        return "User with email: " + user.getEmail() + " is updated.";
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAllByIsActiveTrueAndIsEmailVerifiedTrue();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        if (email.isBlank()) {
            throw new RuntimeException("Error: Email can't be empty.");
        }
        if (userRepository.findByEmail(email).isEmpty()) {
            throw new RuntimeException("Error: User with this email: " + email + " doesn't exist.");
        }
        return userRepository.findByEmail(email);
    }

    @Override
    public long countByRoleAndMedicalFacilityId(String role, Long medicalFacilityId) {
        return userRepository.countByRoleAndMedicalFacilityId(role, medicalFacilityId);
    }
}
