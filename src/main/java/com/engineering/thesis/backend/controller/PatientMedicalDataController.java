package com.engineering.thesis.backend.controller;

import com.engineering.thesis.backend.exception.ResourceNotFoundException;
import com.engineering.thesis.backend.model.PatientMedicalData;
import com.engineering.thesis.backend.model.authentication.PatientMedicalDataRequest;
import com.engineering.thesis.backend.service.PatientMedicalDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/patientsMedicalData")
public class PatientMedicalDataController {
    private final PatientMedicalDataService patientMedicalDataService;

    @Operation(security = {
            @SecurityRequirement(name = "bearer-key")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DOCTOR')")
    @PostMapping
    public ResponseEntity<PatientMedicalData> create(@RequestBody PatientMedicalDataRequest patientMedicalData) throws ResourceNotFoundException {
        return ResponseEntity.ok(patientMedicalDataService.create(patientMedicalData));
    }

    @Operation(security = {
            @SecurityRequirement(name = "bearer-key")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DOCTOR')")
    @PutMapping("/{id}")
    public ResponseEntity<PatientMedicalData> updateById(@PathVariable Long id, @RequestBody PatientMedicalData patientMedicalData) throws ResourceNotFoundException {
        return ResponseEntity.ok(patientMedicalDataService.updateById(id, patientMedicalData));
    }

    @Operation(security = {
            @SecurityRequirement(name = "bearer-key")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DOCTOR') or hasRole('ROLE_PATIENT')")
    @GetMapping("/{id}")
    public ResponseEntity<Optional<PatientMedicalData>> getById(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(patientMedicalDataService.getById(id));
    }

    @Operation(security = {
            @SecurityRequirement(name = "bearer-key")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DOCTOR') or hasRole('ROLE_PATIENT')")
    @GetMapping
    public ResponseEntity<List<PatientMedicalData>> getAll() {
        return ResponseEntity.ok(patientMedicalDataService.getAll());
    }

    @Operation(security = {
            @SecurityRequirement(name = "bearer-key")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DOCTOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteById(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(patientMedicalDataService.deleteById(id));
    }
}