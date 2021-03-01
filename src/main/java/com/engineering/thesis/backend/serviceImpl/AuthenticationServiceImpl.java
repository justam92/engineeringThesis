package com.engineering.thesis.backend.serviceImpl;

import com.engineering.thesis.backend.config.jwt.JwtToken;
import com.engineering.thesis.backend.enums.UserRole;
import com.engineering.thesis.backend.exception.ResourceNotFoundException;
import com.engineering.thesis.backend.model.Doctor;
import com.engineering.thesis.backend.model.Patient;
import com.engineering.thesis.backend.model.User;
import com.engineering.thesis.backend.model.authentication.JwtResponse;
import com.engineering.thesis.backend.model.authentication.LoginRequest;
import com.engineering.thesis.backend.model.authentication.RegisterRequest;
import com.engineering.thesis.backend.repository.UserRepository;
import com.engineering.thesis.backend.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.engineering.thesis.backend.enums.UserRole.DOCTOR;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtToken jwtToken;
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final EmailSenderService emailSenderService;
    private final MedicalFacilityService medicalFacilityService;
    @Value("${frontend.host}")
    private String frontendHost;
    @Value("${frontend.port}")
    private String frontendPort;

    @Transactional
    @Override
    public User register(RegisterRequest signUpRequest) throws ResourceNotFoundException {
        if (signUpRequest == null) {
            throw new RuntimeException("Error: Fields to registration account can't be empty!");
        }

        User user = new User();
        if (userRepository.existsByEmailAndIsActive(signUpRequest.getEmail(), true)) {
            throw new RuntimeException("Error: Email is already in use!");
        }
        if (signUpRequest.getRole().isBlank()) {
            throw new RuntimeException("Error: You should choose one of the roles!");
        }

        UserRole userRole = UserRole.parse(signUpRequest.getRole());
        save(signUpRequest, user, userRole);
        saveRelated(user);
        sendRegistrationConfirmationEmail(user);
        log.info("Registered successfully! " + user.getName() + " check your e-mail to complete registration.");
        return user;
    }

    private void save(RegisterRequest signUpRequest, User user, UserRole userRole) throws ResourceNotFoundException {
        medicalFacilityService.usersCounter(userRole.name(), signUpRequest.getMedicalFacilityId());
        if(medicalFacilityService.getById(signUpRequest.getMedicalFacilityId()).isEmpty()){
            throw new RuntimeException("Error: Medical Facility id can't be empty.");
        }
        user.setRole(userRole.name());
        user.setName(signUpRequest.getName());
        user.setSurname(signUpRequest.getSurname());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setMedicalFacility(medicalFacilityService.getById(signUpRequest.getMedicalFacilityId()).get());
        user.setActive(false);
        userRepository.save(user);
    }

    private void saveRelated(User user) throws ResourceNotFoundException {
        if (user.getRole().equals(DOCTOR.name())) {
            Doctor doctor = new Doctor();
            doctor.setUser(user);
            doctorService.create(doctor);
        } else {
            Patient patient = new Patient();
            patient.setUser(user);
            patientService.create(patient);
        }
    }

    @Override
    public JwtResponse login(LoginRequest data) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        data.getEmail(),
                        data.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtToken.generateLoginJwtToken(authentication);

        User user = (User) authentication.getPrincipal();
        if (!user.isEmailVerified()) {
            throw new RuntimeException("Error: User's email isn't verified!");
        }
        return new JwtResponse(token,
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getMedicalFacility());
    }

    @Override
    public String confirmAccount(String confirmationToken) throws ResourceNotFoundException {
        if (confirmationToken.isBlank()) {
            throw new RuntimeException("Error: Invalid token!");
        }
        User user = userRepository.findByEmail(jwtToken.getUserNameFromJwtToken(confirmationToken))
                .orElseThrow(() -> new ResourceNotFoundException("Error: User with this email : " + jwtToken.getUserNameFromJwtToken(confirmationToken) + " doesn't exist."));
        user.setActive(true);
        user.setEmailVerified(true);

        userRepository.save(user);
        return "Register completed!";
    }

    private void sendRegistrationConfirmationEmail(User user) {
        String token = jwtToken.generateRegistrationConfirmationJwtToken(user);
        emailSenderService.sendEmail(user.getEmail(),
                "Account registration confirmation",
                "Hello " + user.getName() + "! \n \nYou've received an email because a new account has been created. To confirm your account, please click here: "
                        + "http://" + frontendHost + ":" + frontendPort + "/confirm-account?token=" + token
                        + "\n \nIf that wasn't you, please ignore this email.");
    }
}