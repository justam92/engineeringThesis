package com.engineering.thesis.backend.model.authentication;

import com.engineering.thesis.backend.model.MedicalFacility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
public class JwtResponse {
    @NotBlank(message = "Token can't be empty")
    private String token;
    @NotNull(message = "Id can't be null")
    private Long id;
    @NotBlank(message = "Name can't be empty")
    private String name;
    @NotBlank(message = "Surname can't be empty")
    private String surname;
    @NotBlank(message = "Username can't be empty")
    private String username;
    @NotBlank(message = "Email can't be empty")
    private String email;
    @NotBlank(message = "Role can't be empty")
    private String role;
    @NotNull(message = "Medical Facility can't be empty")
    private MedicalFacility medicalFacility;
}