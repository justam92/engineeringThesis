package com.engineering.thesis.backend.serviceImpl;

import com.engineering.thesis.backend.exception.ResourceNotFoundException;
import com.engineering.thesis.backend.model.MedicalFacility;
import com.engineering.thesis.backend.model.authentication.MedicalFacilityRequest;
import com.engineering.thesis.backend.repository.MedicalFacilityRepository;
import com.engineering.thesis.backend.service.MedicalFacilityService;
import com.engineering.thesis.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.engineering.thesis.backend.enums.UserRole.DOCTOR;
import static com.engineering.thesis.backend.enums.UserRole.PATIENT;

@Service
@RequiredArgsConstructor
public class MedicalFacilityServiceImpl implements MedicalFacilityService {
    private final MedicalFacilityRepository medicalFacilityRepository;
    private final UserService userService;

    @Override
    public MedicalFacility create(MedicalFacilityRequest medicalFacility) {
        if (medicalFacility == null) {
            throw new RuntimeException("Error: Medical Facility fields can't be empty.");
        }
        return medicalFacilityRepository.save(MedicalFacility.builder()
                .name(medicalFacility.getName())
                .localization(medicalFacility.getLocalization())
                .contactNumber(medicalFacility.getContactNumber())
                .build());
    }

    @Override
    public MedicalFacility updateById(Long id, MedicalFacility medicalFacility) throws ResourceNotFoundException {
        if (id == null) {
            throw new RuntimeException("Error: Id can't be empty.");
        }
        if (medicalFacility == null) {
            throw new RuntimeException("Error: Medical Facility fields can't be empty.");
        }
        MedicalFacility medicalFacilityToUpdate = medicalFacilityRepository.findById(medicalFacility.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Error: Medical Facility with id: " + medicalFacility.getId() + " doesn't exists."));
        medicalFacilityToUpdate.setContactNumber(medicalFacility.getContactNumber());
        medicalFacilityToUpdate.setLocalization(medicalFacility.getLocalization());
        return medicalFacilityRepository.save(medicalFacilityToUpdate);
    }

    @Override
    public Long deleteById(Long id) throws ResourceNotFoundException {
        if (id == null) {
            throw new RuntimeException("Error: Id can't be empty.");
        }
        if (medicalFacilityRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Error: Medical Facility with id: " + id + " doesn't exists.");
        }
        medicalFacilityRepository.deleteById(id);
        return id;
    }

    @Override
    public List<MedicalFacility> getAll() {
        return medicalFacilityRepository.findAll();
    }

    @Override
    public Optional<MedicalFacility> getById(Long id) throws ResourceNotFoundException {
        if (id == null) {
            throw new RuntimeException("Error: Id can't be empty.");
        }
        if (medicalFacilityRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Error: Medical Facility with id: " + id + " doesn't exists.");
        }
        return medicalFacilityRepository.findById(id);
    }

    @Override
    public void usersCounter(String userRole, Long id) {
        if (id == null) {
            throw new RuntimeException("Error: Id can't be empty.");
        }
        if (userRole == null) {
            throw new RuntimeException("Error: User role can't be empty.");
        }
        long usersCount;
        if (medicalFacilityRepository.findById(id).isEmpty()) {
            throw new RuntimeException("Error: Medical Facility with id: " + id + " doesn't exists.");
        }
        MedicalFacility medicalFacility = medicalFacilityRepository.findById(id).get();
        if (userRole.equals(DOCTOR.name())) {
            usersCount = userService.countByRoleAndMedicalFacilityId(DOCTOR.name(), id);
            medicalFacility.setDoctorCount(usersCount);
        } else if (userRole.equals(PATIENT.name())) {
            usersCount = userService.countByRoleAndMedicalFacilityId(PATIENT.name(), id);
            medicalFacility.setPatientCount(usersCount);
        }
        medicalFacilityRepository.save(medicalFacility);
    }
}