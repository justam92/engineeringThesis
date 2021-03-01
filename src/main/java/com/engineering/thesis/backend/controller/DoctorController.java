package com.engineering.thesis.backend.controller;

import com.engineering.thesis.backend.exception.ResourceNotFoundException;
import com.engineering.thesis.backend.model.Doctor;
import com.engineering.thesis.backend.model.User;
import com.engineering.thesis.backend.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

import static com.engineering.thesis.backend.enums.UserRole.DOCTOR;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/doctors")
public class DoctorController {
    private final DoctorService doctorService;

    @Operation(security = {
            @SecurityRequirement(name = "bearer-key")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DOCTOR')")
    @PutMapping
    public ResponseEntity<String> update(@RequestBody Doctor doctor, HttpServletRequest request) throws ResourceNotFoundException {
        Authentication authentication = (Authentication) request.getUserPrincipal();
        User user = (User) authentication.getPrincipal();
        String userEmail = user.getEmail();
        if (user.getRole().equals(DOCTOR.getRole()) && !userEmail.equals(doctor.getUser().getEmail())) {
            return ResponseEntity.badRequest().body("Error: Doctor's email isn't same!");
        }
        return ResponseEntity.ok(doctorService.update(doctor));
    }

    @Operation(security = {
            @SecurityRequirement(name = "bearer-key")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DOCTOR') or hasRole('ROLE_PATIENT')")
    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getById(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(doctorService.getById(id));
    }

    @Operation(security = {
            @SecurityRequirement(name = "bearer-key")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DOCTOR') or hasRole('ROLE_PATIENT')")
    @GetMapping
    public ResponseEntity<List<Doctor>> getAll() {
        return ResponseEntity.ok(doctorService.getAll());
    }

    @Operation(security = {
            @SecurityRequirement(name = "bearer-key")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DOCTOR')")
    @PostMapping(value = "/sendMedicalExaminationsResult", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> sendMedicalExaminationsResult(@RequestParam("file") MultipartFile file,
                                                                @RequestParam("patientEmail") String patientEmail) throws IOException, ResourceNotFoundException {
        return ResponseEntity.ok(doctorService.sendMedicalExaminationsResult(file, patientEmail));
    }
}