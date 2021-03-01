package com.engineering.thesis.backend.model.authentication;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalFacilityRequest {
    @NotBlank(message = "Name can't be empty")
    private String name;
    @NotBlank(message = "Localization can't be empty")
    private String localization;
    @NotBlank(message = "Phone can't be empty")
    @Pattern(regexp = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]{8,}$",
            message = "Phone number is invalid")
    private String contactNumber;
}
