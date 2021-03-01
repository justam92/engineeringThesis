package com.engineering.thesis.backend.service;

import com.engineering.thesis.backend.exception.ResourceNotFoundException;
import com.engineering.thesis.backend.model.Appointment;
import com.engineering.thesis.backend.model.authentication.AppointmentRequest;

import java.util.List;
import java.util.Optional;

public interface AppointmentService {
    Appointment create(AppointmentRequest appointment) throws ResourceNotFoundException;
    Appointment updateById(Long id, Appointment appointment) throws ResourceNotFoundException;
    Long deleteById(Long id) throws ResourceNotFoundException;
    List<Appointment> getAll();
    Optional<Appointment> getById(Long id) throws ResourceNotFoundException;
}