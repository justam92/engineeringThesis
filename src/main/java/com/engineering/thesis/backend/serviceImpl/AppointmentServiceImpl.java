package com.engineering.thesis.backend.serviceImpl;

import com.engineering.thesis.backend.exception.ResourceNotFoundException;
import com.engineering.thesis.backend.model.Appointment;
import com.engineering.thesis.backend.model.authentication.AppointmentRequest;
import com.engineering.thesis.backend.repository.AppointmentRepository;
import com.engineering.thesis.backend.service.AppointmentService;
import com.engineering.thesis.backend.service.DoctorService;
import com.engineering.thesis.backend.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientService patientService;
    private final DoctorService doctorService;

    @Override
    public Appointment create(AppointmentRequest appointmentRequest) throws ResourceNotFoundException {
        if (appointmentRequest == null) {
            throw new RuntimeException("Error: Appointment fields can't be empty.");
        }
        if (doctorService.getById(appointmentRequest.getDoctorId()) == null) {
            throw new RuntimeException("Error: Doctor with id: " + appointmentRequest.getDoctorId() + " doesn't exist.");
        }
        if (patientService.getById(appointmentRequest.getPatientId()) == null) {
            throw new RuntimeException("Error: Patient with id: " + appointmentRequest.getPatientId() + " doesn't exist.");
        }
        if (appointmentRequest.getAppointmentDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Error: Date is past.");
        }
        return appointmentRepository.save(Appointment.builder()
                .doctor(doctorService.getById(appointmentRequest.getDoctorId()))
                .patient(patientService.getById(appointmentRequest.getPatientId()))
                .cost(appointmentRequest.getCost())
                .appointmentDate(appointmentRequest.getAppointmentDate())
                .build());
    }

    @Override
    public Appointment updateById(Long id, Appointment appointment) throws ResourceNotFoundException {
        if (id == null) {
            throw new RuntimeException("Error: Id can't be empty.");
        }
        if (appointment == null) {
            throw new RuntimeException("Error: Appointment fields can't be empty.");
        }
        Appointment appointmentToUpdate = appointmentRepository.findById(appointment.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Error: Appointment with id: " + appointment.getId() + " doesn't exists."));
        if (doctorService.getById(appointment.getDoctor().getId()) == null) {
            throw new RuntimeException("Error: Doctor with id: " + appointment.getDoctor().getId() + " doesn't exist.");
        }
        if (patientService.getById(appointment.getPatient().getId()) == null) {
            throw new RuntimeException("Error: Patient with id: " + appointment.getDoctor().getId() + " doesn't exist.");
        }
        if (appointment.getAppointmentDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Error: Date is past.");
        }
        appointmentToUpdate.setPatient(appointment.getPatient());
        appointmentToUpdate.setDoctor(appointment.getDoctor());
        appointmentToUpdate.setCost(appointment.getCost());
        appointmentToUpdate.setAppointmentDate(appointment.getAppointmentDate());
        return appointmentRepository.save(appointmentToUpdate);
    }

    @Override
    public Long deleteById(Long id) throws ResourceNotFoundException {
        if (id == null) {
            throw new RuntimeException("Error: Id can't be empty.");
        }
        Optional<Appointment> appointment = appointmentRepository.findById(id);
        if (appointment.isEmpty()) {
            throw new ResourceNotFoundException("Error: Appointment with id: " + id + " doesn't exists.");
        }
        appointmentRepository.deleteById(id);
        return id;
    }

    @Override
    public List<Appointment> getAll() {
        return appointmentRepository.findAll();
    }

    @Override
    public Optional<Appointment> getById(Long id) throws ResourceNotFoundException {
        if (id == null) {
            throw new RuntimeException("Error: Id can't be empty.");
        }
        if (appointmentRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Error: Appointment with id: " + id + " doesn't exists.");
        }
        return appointmentRepository.findById(id);
    }
}