package com.engineering.thesis.backend.model.authentication;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientMedicalDataRequest {
    @NotNull(message = "Patient id can't be empty")
    private Long patientId;
    @NotNull(message = "Doctor id can't be empty")
    private Long doctorId;
    @NotNull(message = "Date can't be empty")
    private LocalDateTime treatmentDate;
    @NotBlank(message = "Medical procedure can't be empty")
    private String medicalProcedure;
    @NotBlank(message = "Additional notes can't be empty")
    private String additionalNotes;
}
