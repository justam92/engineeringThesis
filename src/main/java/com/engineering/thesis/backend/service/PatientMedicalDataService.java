package com.engineering.thesis.backend.service;

import com.engineering.thesis.backend.exception.ResourceNotFoundException;
import com.engineering.thesis.backend.model.PatientMedicalData;
import com.engineering.thesis.backend.model.authentication.PatientMedicalDataRequest;

import java.util.List;
import java.util.Optional;

public interface PatientMedicalDataService {
    PatientMedicalData create(PatientMedicalDataRequest patientMedicalData) throws ResourceNotFoundException;
    PatientMedicalData updateById(Long id, PatientMedicalData patientMedicalData) throws ResourceNotFoundException;
    Long deleteById(Long id) throws ResourceNotFoundException;
    List<PatientMedicalData> getAll();
    Optional<PatientMedicalData> getById(Long id) throws ResourceNotFoundException;
}