package com.engineering.thesis.backend.controller;

import com.engineering.thesis.backend.exception.ResourceNotFoundException;
import com.engineering.thesis.backend.model.MedicalFacility;
import com.engineering.thesis.backend.model.authentication.MedicalFacilityRequest;
import com.engineering.thesis.backend.service.MedicalFacilityService;
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
@RequestMapping
public class MedicalFacilityController {
    private final MedicalFacilityService medicalFacilityService;

    @Operation(security = {
            @SecurityRequirement(name = "bearer-key")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @PostMapping("api/medicalFacilities")
    public ResponseEntity<MedicalFacility> create(@RequestBody MedicalFacilityRequest medicalFacility) throws ResourceNotFoundException {
        return ResponseEntity.ok(medicalFacilityService.create(medicalFacility));
    }

    @Operation(security = {
            @SecurityRequirement(name = "bearer-key")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @PutMapping("api/medicalFacilities/{id}")
    public ResponseEntity<MedicalFacility> updateById(@PathVariable Long id, @RequestBody MedicalFacility medicalFacility) throws ResourceNotFoundException {
        return ResponseEntity.ok(medicalFacilityService.updateById(id, medicalFacility));
    }

    @GetMapping("medicalFacilities/{id}")
    public ResponseEntity<Optional<MedicalFacility>> getById(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(medicalFacilityService.getById(id));
    }

    @GetMapping("medicalFacilities")
    public ResponseEntity<List<MedicalFacility>> getAll() {
        return ResponseEntity.ok(medicalFacilityService.getAll());
    }

    @Operation(security = {
            @SecurityRequirement(name = "bearer-key")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @DeleteMapping("api/medicalFacilities/{id}")
    public ResponseEntity<Long> deleteById(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(medicalFacilityService.deleteById(id));
    }
}