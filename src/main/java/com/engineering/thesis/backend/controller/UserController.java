package com.engineering.thesis.backend.controller;

import com.engineering.thesis.backend.exception.ResourceNotFoundException;
import com.engineering.thesis.backend.model.User;
import com.engineering.thesis.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

import static com.engineering.thesis.backend.enums.UserRole.DOCTOR;
import static com.engineering.thesis.backend.enums.UserRole.PATIENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;

    @Operation(security = {
            @SecurityRequirement(name = "bearer-key")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @Operation(security = {
            @SecurityRequirement(name = "bearer-key")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Optional<User>> getById(@PathVariable(value = "id") Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(userService.getById(id));
    }

    @Operation(security = {
            @SecurityRequirement(name = "bearer-key")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DOCTOR') or hasRole('ROLE_PATIENT')")
    @DeleteMapping("/{email}/deactivate")
    public ResponseEntity<String> deactivateByEmail(@PathVariable(value = "email") String email, HttpServletRequest request) throws ResourceNotFoundException {
        Authentication authentication = (Authentication) request.getUserPrincipal();
        User user = (User) authentication.getPrincipal();
        String userEmail = user.getEmail();
        if (user.getRole().equals(DOCTOR.getRole()) || user.getRole().equals(PATIENT.getRole())) {
            if (!userEmail.equals(email)) {
                return ResponseEntity.badRequest().body("Error: User's email isn't same!");
            }
        }
        return ResponseEntity.ok(userService.deactivateByEmail(email));
    }
}