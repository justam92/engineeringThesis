package com.engineering.thesis.backend.service;

import com.engineering.thesis.backend.exception.ResourceNotFoundException;
import com.engineering.thesis.backend.model.User;
import com.engineering.thesis.backend.model.authentication.JwtResponse;
import com.engineering.thesis.backend.model.authentication.LoginRequest;
import com.engineering.thesis.backend.model.authentication.RegisterRequest;

public interface AuthenticationService {
    User register(RegisterRequest signUpRequest) throws ResourceNotFoundException;
    JwtResponse login(LoginRequest data);
    String confirmAccount(String confirmationToken) throws ResourceNotFoundException;
}
