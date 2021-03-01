package com.engineering.thesis.backend.service;

import com.engineering.thesis.backend.exception.ResourceNotFoundException;
import com.engineering.thesis.backend.model.Patient;

import java.util.List;
import java.util.Optional;

public interface PatientService {
    Patient create(Patient patient) throws ResourceNotFoundException;
    String update(Patient patient) throws ResourceNotFoundException;
    List<Patient> getAll();
    Patient getById(Long id) throws ResourceNotFoundException;
    Optional<Patient> getByUserId(Long userId) throws ResourceNotFoundException;
}