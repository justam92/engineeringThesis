package com.engineering.thesis.backend.model.authentication;

import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceRequest {
    @NotBlank(message = "Treatment can't be empty")
    private String treatment;
    @Range(min = 0, message = "Price must be at least {min}")
    private Long price;
}
