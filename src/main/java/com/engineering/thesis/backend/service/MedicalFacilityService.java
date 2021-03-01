package com.engineering.thesis.backend.service;

import com.engineering.thesis.backend.exception.ResourceNotFoundException;
import com.engineering.thesis.backend.model.MedicalFacility;
import com.engineering.thesis.backend.model.authentication.MedicalFacilityRequest;

import java.util.List;
import java.util.Optional;

public interface MedicalFacilityService {
    MedicalFacility create(MedicalFacilityRequest medicalFacility) throws ResourceNotFoundException;
    MedicalFacility updateById(Long id, MedicalFacility medicalFacility) throws ResourceNotFoundException;
    Long deleteById(Long id) throws ResourceNotFoundException;
    List<MedicalFacility> getAll();
    Optional<MedicalFacility> getById(Long id) throws ResourceNotFoundException;
    void usersCounter(String userRole, Long id);
}