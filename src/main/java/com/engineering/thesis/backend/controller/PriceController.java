package com.engineering.thesis.backend.controller;

import com.engineering.thesis.backend.exception.ResourceNotFoundException;
import com.engineering.thesis.backend.model.Price;
import com.engineering.thesis.backend.model.authentication.PriceRequest;
import com.engineering.thesis.backend.service.PriceService;
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
@RequestMapping("api/prices")
public class PriceController {
    private final PriceService priceService;

    @Operation(security = {
            @SecurityRequirement(name = "bearer-key")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @PostMapping
    public ResponseEntity<Price> create(@RequestBody PriceRequest price) throws ResourceNotFoundException {
        return ResponseEntity.ok(priceService.create(price));
    }

    @Operation(security = {
            @SecurityRequirement(name = "bearer-key")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<Price> updateById(@PathVariable Long id, @RequestBody Price price) throws ResourceNotFoundException {
        return ResponseEntity.ok(priceService.updateById(id, price));
    }

    @Operation(security = {
            @SecurityRequirement(name = "bearer-key")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DOCTOR') or hasRole('ROLE_PATIENT')")
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Price>> getById(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(priceService.getById(id));
    }

    @Operation(security = {
            @SecurityRequirement(name = "bearer-key")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DOCTOR') or hasRole('ROLE_PATIENT')")
    @GetMapping
    public ResponseEntity<List<Price>> getAll() {
        return ResponseEntity.ok(priceService.getAll());
    }

    @Operation(security = {
            @SecurityRequirement(name = "bearer-key")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteById(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(priceService.deleteById(id));
    }
}