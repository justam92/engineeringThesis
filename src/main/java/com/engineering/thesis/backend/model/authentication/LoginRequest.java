package com.engineering.thesis.backend.model.authentication;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class LoginRequest {
    @NotBlank(message = "Email can't be empty")
    @Email(message = "Invalid email address")
    private String email;
    @NotBlank(message = "Password can't be empty")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$!%^*?&])[A-Za-z\\d@$!%*?&].*$",
            message = "Password should have at least one uppercase letter, one lowercase letter, one number and one special character")
    private String password;
}