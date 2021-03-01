package com.engineering.thesis.backend.service;

import com.engineering.thesis.backend.exception.ResourceNotFoundException;
import com.engineering.thesis.backend.model.Doctor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface DoctorService {
    Doctor create(Doctor doctor) throws ResourceNotFoundException;
    String update(Doctor doctor) throws ResourceNotFoundException;
    List<Doctor> getAll();
    Doctor getById(Long id) throws ResourceNotFoundException;
    Optional<Doctor> getByUserId(Long userId) throws ResourceNotFoundException;
    String sendMedicalExaminationsResult(MultipartFile file, String patientEmail) throws IOException, ResourceNotFoundException;
}