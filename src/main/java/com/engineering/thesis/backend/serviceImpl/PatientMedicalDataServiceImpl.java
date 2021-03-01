package com.engineering.thesis.backend.serviceImpl;

import com.engineering.thesis.backend.exception.ResourceNotFoundException;
import com.engineering.thesis.backend.model.PatientMedicalData;
import com.engineering.thesis.backend.model.authentication.PatientMedicalDataRequest;
import com.engineering.thesis.backend.repository.PatientMedicalDataRepository;
import com.engineering.thesis.backend.service.DoctorService;
import com.engineering.thesis.backend.service.PatientMedicalDataService;
import com.engineering.thesis.backend.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientMedicalDataServiceImpl implements PatientMedicalDataService {
    private final PatientMedicalDataRepository patientMedicalDataRepository;
    private final PatientService patientService;
    private final DoctorService doctorService;

    @Transactional
    @Override
    public PatientMedicalData create(PatientMedicalDataRequest patientMedicalDataRequest) throws ResourceNotFoundException {
        if (patientMedicalDataRequest == null) {
            throw new RuntimeException("Error: Patient Medical Data fields can't be empty.");
        }
        if (doctorService.getById(patientMedicalDataRequest.getDoctorId()) == null) {
            throw new RuntimeException("Error: Doctor with id: " + patientMedicalDataRequest.getDoctorId() + " doesn't exist.");
        }
        if (patientService.getById(patientMedicalDataRequest.getPatientId()) == null) {
            throw new RuntimeException("Error: Patient with id: " + patientMedicalDataRequest.getPatientId() + " doesn't exist.");
        }
        if (patientMedicalDataRequest.getTreatmentDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Error: Date is past.");
        }
        return patientMedicalDataRepository.save(PatientMedicalData.builder()
                .doctor(doctorService.getById(patientMedicalDataRequest.getDoctorId()))
                .patient(patientService.getById(patientMedicalDataRequest.getPatientId()))
                .treatmentDate(patientMedicalDataRequest.getTreatmentDate())
                .medicalProcedure(patientMedicalDataRequest.getMedicalProcedure())
                .additionalNotes(patientMedicalDataRequest.getAdditionalNotes())
                .build());
    }

    @Override
    public PatientMedicalData updateById(Long id, PatientMedicalData patientMedicalData) throws ResourceNotFoundException {
        if (id == null) {
            throw new RuntimeException("Error: Id can't be empty.");
        }
        if (patientMedicalData == null) {
            throw new RuntimeException("Error: Patient Medical Data fields can't be empty.");
        }
        PatientMedicalData patientMedicalDataToUpdate = patientMedicalDataRepository.findById(patientMedicalData.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Error: Patient Medical Data with id: " + patientMedicalData.getId() + " doesn't exists."));
        if (patientMedicalData.getTreatmentDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Error: Date is past.");
        }
        patientMedicalDataToUpdate.setPatient(patientMedicalData.getPatient());
        patientMedicalDataToUpdate.setDoctor(patientMedicalData.getDoctor());
        patientMedicalDataToUpdate.setTreatmentDate(patientMedicalData.getTreatmentDate());
        patientMedicalDataToUpdate.setMedicalProcedure(patientMedicalData.getMedicalProcedure());
        patientMedicalDataToUpdate.setAdditionalNotes(patientMedicalData.getAdditionalNotes());
        return patientMedicalDataRepository.save(patientMedicalDataToUpdate);
    }

    @Override
    public Long deleteById(Long id) throws ResourceNotFoundException {
        if (id == null) {
            throw new RuntimeException("Error: Id can't be empty.");
        }
        if (patientMedicalDataRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Error: Patient Medical Data with id: " + id + " doesn't exists.");
        }
        patientMedicalDataRepository.deleteById(id);
        return id;
    }

    @Override
    public List<PatientMedicalData> getAll() {
        return patientMedicalDataRepository.findAll();
    }

    @Override
    public Optional<PatientMedicalData> getById(Long id) throws ResourceNotFoundException {
        if (id == null) {
            throw new RuntimeException("Error: Id can't be empty.");
        }
        Optional<PatientMedicalData> patientMedicalData = patientMedicalDataRepository.findById(id);
        if (patientMedicalData.isEmpty()) {
            throw new ResourceNotFoundException("Error: PatientMedicalData with id: " + id + " doesn't exists.");
        }
        return patientMedicalData;
    }
}