package com.engineering.thesis.backend.serviceImpl;

import com.engineering.thesis.backend.exception.ResourceNotFoundException;
import com.engineering.thesis.backend.model.Doctor;
import com.engineering.thesis.backend.model.User;
import com.engineering.thesis.backend.repository.DoctorRepository;
import com.engineering.thesis.backend.service.DoctorService;
import com.engineering.thesis.backend.service.EmailSenderService;
import com.engineering.thesis.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final EmailSenderService emailSenderService;
    private final UserService userService;

    @Override
    public Doctor create(Doctor doctor) throws ResourceNotFoundException {
        if (doctor == null) {
            throw new RuntimeException("Error: Doctor fields can't be empty.");
        }
        if (doctor.getId() != null) {
            if (doctorRepository.findById(doctor.getId()).isPresent()) {
                throw new ResourceNotFoundException("Error: Doctor with id: " + doctor.getId() + " already exists.");
            }
        }
        return doctorRepository.save(doctor);
    }

    @Transactional
    @Override
    public String update(Doctor doctor) throws ResourceNotFoundException {
        if (doctor == null) {
            throw new RuntimeException("Error: Doctor fields can't be empty.");
        }
        Doctor doctorToUpdate = doctorRepository.findById(doctor.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Error: Doctor with id: " + doctor.getId() + " doesn't exists."));
        doctorToUpdate.setSpecialist(doctor.getSpecialist());
        userService.update(doctor.getUser());
        doctorRepository.save(doctorToUpdate);
        return "Doctor is updated.";
    }

    @Override
    public List<Doctor> getAll() {
        return doctorRepository.findAll();
    }

    @Override
    public Doctor getById(Long id) throws ResourceNotFoundException {
        if (id == null) {
            throw new RuntimeException("Error: Id can't be empty.");
        }
        if (doctorRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Error: Doctor with id: " + id + " doesn't  exists.");
        }
        return doctorRepository.findById(id).get();
    }

    @Override
    public Optional<Doctor> getByUserId(Long userId) throws ResourceNotFoundException {
        if (userId == null) {
            throw new RuntimeException("Error: Id can't be empty.");
        }
        if (doctorRepository.findByUserId(userId).isEmpty()) {
            throw new ResourceNotFoundException("Error: User with id: " + userId + " doesn't  exists.");
        }
        return doctorRepository.findByUserId(userId);
    }

    @Override
    public String sendMedicalExaminationsResult(MultipartFile file, String patientEmail) throws IOException, ResourceNotFoundException {
        User user = userService.findByEmail(patientEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Error: User not found for this email : " + patientEmail));
        if (user.isActive() && user.isEmailVerified() && user.getRole().equals("PATIENT")) {
            try (InputStream inputStream = file.getInputStream()) {
                emailSenderService.sendEmailWithAttachment(user.getEmail(),
                        "Test result",
                        "Hello " + user.getName() + "\n \nIn attachment you've test results.",
                        file.getOriginalFilename(),
                        parseInputStreamToPDFDataSource(inputStream));
            }
        }
        return "Message with attachment has been sent successfully.";
    }

    private DataSource parseInputStreamToPDFDataSource(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        inputStream.transferTo(outputStream);
        return new ByteArrayDataSource(outputStream.toByteArray(), "application/pdf");
    }
}