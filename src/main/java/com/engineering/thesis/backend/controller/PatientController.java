package com.engineering.thesis.backend.controller;

import com.engineering.thesis.backend.exception.ResourceNotFoundException;
import com.engineering.thesis.backend.model.Patient;
import com.engineering.thesis.backend.model.User;
import com.engineering.thesis.backend.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.engineering.thesis.backend.enums.UserRole.DOCTOR;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/patients")
public class PatientController {
    private final PatientService patientService;

    @Operation(security = {
            @SecurityRequirement(name = "bearer-key")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_PATIENT')")
    @PutMapping
    public ResponseEntity<String> update(@RequestBody Patient patient, HttpServletRequest request) throws ResourceNotFoundException {
        Authentication authentication = (Authentication) request.getUserPrincipal();
        User user = (User) authentication.getPrincipal();
        String userEmail = user.getEmail();
        if (user.getRole().equals(DOCTOR.getRole()) && !userEmail.equals(patient.getUser().getEmail())) {
            return ResponseEntity.badRequest().body("Error: Patient's email isn't same!");
        }
        return ResponseEntity.ok(patientService.update(patient));
    }

    @Operation(security = {
            @SecurityRequirement(name = "bearer-key")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DOCTOR')")
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getById(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(patientService.getById(id));
    }

    @Operation(security = {
            @SecurityRequirement(name = "bearer-key")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DOCTOR')")
    @GetMapping
    public ResponseEntity<List<Patient>> getAll() {
        return ResponseEntity.ok(patientService.getAll());
    }
}