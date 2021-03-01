package com.engineering.thesis.backend.model.authentication;

import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequest {
    @NotNull(message = "Patient id can't be empty.")
    private long patientId;
    @NotNull(message = "Doctor id can't be empty.")
    private long doctorId;
    @Range(min = 0, message = "Cost must be at least {min}.")
    @NotNull(message = "Cost appointment can't be empty.")
    private long cost;
    @NotNull(message = "Date can't be empty.")
    private LocalDateTime appointmentDate;
}
