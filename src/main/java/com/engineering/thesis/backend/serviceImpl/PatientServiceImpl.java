package com.engineering.thesis.backend.serviceImpl;

import com.engineering.thesis.backend.exception.ResourceNotFoundException;
import com.engineering.thesis.backend.model.Patient;
import com.engineering.thesis.backend.repository.PatientRepository;
import com.engineering.thesis.backend.service.PatientService;
import com.engineering.thesis.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final UserService userService;

    @Override
    public Patient create(Patient patient) throws ResourceNotFoundException {
        if (patient == null) {
            throw new RuntimeException("Error: Patient fields can't be empty.");
        }
        if (patient.getId() != null) {
            if (patientRepository.findById(patient.getId()).isPresent()) {
                throw new ResourceNotFoundException("Error: Patient with id: " + patient.getId() + " already exists.");
            }
        }
        return patientRepository.save(patient);
    }

    @Transactional
    @Override
    public String update(Patient patient) throws ResourceNotFoundException {
        if (patient == null) {
            throw new RuntimeException("Error: Patient fields can't be empty.");
        }
        Patient patientToUpdate = patientRepository.findById(patient.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Error: Patient with id: " + patient.getId() + " doesn't exists."));
        patientToUpdate.setInsured(patient.isInsured());
        userService.update(patient.getUser());
        patientRepository.save(patientToUpdate);
        return "Patient is updated.";
    }

    @Override
    public List<Patient> getAll() {
        return patientRepository.findAll();
    }

    @Override
    public Patient getById(Long id) throws ResourceNotFoundException {
        if (id == null) {
            throw new RuntimeException("Error: Id can't be empty.");
        }
        if (patientRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Error: Patient with id: " + id + " doesn't exists.");
        }
        return patientRepository.findById(id).get();
    }

    @Override
    public Optional<Patient> getByUserId(Long userId) throws ResourceNotFoundException {
        if (userId == null) {
            throw new RuntimeException("Error: Id can't be empty.");
        }
        if (patientRepository.findByUserId(userId).isEmpty()) {
            throw new ResourceNotFoundException("Error: User with id: " + userId + " doesn't exists.");
        }
        return patientRepository.findByUserId(userId);
    }
}