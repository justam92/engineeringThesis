package com.engineering.thesis.backend.controller;

import com.engineering.thesis.backend.config.jwt.JwtToken;
import com.engineering.thesis.backend.exception.ResourceNotFoundException;
import com.engineering.thesis.backend.model.User;
import com.engineering.thesis.backend.model.authentication.JwtResponse;
import com.engineering.thesis.backend.model.authentication.LoginRequest;
import com.engineering.thesis.backend.model.authentication.RegisterRequest;
import com.engineering.thesis.backend.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final JwtToken jwtToken;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest data) {
        return ResponseEntity.ok(authenticationService.login(data));
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterRequest signUpRequest) throws ResourceNotFoundException {
        return ResponseEntity.ok(authenticationService.register(signUpRequest));
    }

    @GetMapping("/confirm-account")
    public ResponseEntity<String> confirmAccount(@RequestParam("token") String confirmationToken) throws ResourceNotFoundException {
        return ResponseEntity.ok(authenticationService.confirmAccount(confirmationToken));
    }

    @Operation(security = {
            @SecurityRequirement(name = "bearer-key")
    })
    @GetMapping("/refreshToken")
    public ResponseEntity<String> refreshToken(HttpServletRequest request) {
        String userEmail = (String) request.getAttribute("userEmail");
        return ResponseEntity.ok(jwtToken.generateRefreshToken(userEmail));
    }
}
