package com.engineering.thesis.backend.controller;

import com.engineering.thesis.backend.exception.ResourceNotFoundException;
import com.engineering.thesis.backend.model.Appointment;
import com.engineering.thesis.backend.model.authentication.AppointmentRequest;
import com.engineering.thesis.backend.service.AppointmentService;
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
@PreAuthorize("hasRole('ROLE_DOCTOR') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_PATIENT')")
@RequestMapping("api/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    @Operation(security = {
            @SecurityRequirement(name = "bearer-key")
    })
    @PostMapping
    public ResponseEntity<Appointment> create(@RequestBody AppointmentRequest appointment) throws ResourceNotFoundException {
        return ResponseEntity.ok(appointmentService.create(appointment));
    }

    @Operation(security = {
            @SecurityRequirement(name = "bearer-key")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateById(@PathVariable Long id, @RequestBody Appointment appointment) throws ResourceNotFoundException {
        return ResponseEntity.ok(appointmentService.updateById(id, appointment));
    }

    @Operation(security = {
            @SecurityRequirement(name = "bearer-key")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Appointment>> getById(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(appointmentService.getById(id));
    }

    @Operation(security = {
            @SecurityRequirement(name = "bearer-key")
    })
    @GetMapping
    public ResponseEntity<List<Appointment>> getAll() {
        return ResponseEntity.ok(appointmentService.getAll());
    }

    @Operation(security = {
            @SecurityRequirement(name = "bearer-key")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteById(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(appointmentService.deleteById(id));
    }
}