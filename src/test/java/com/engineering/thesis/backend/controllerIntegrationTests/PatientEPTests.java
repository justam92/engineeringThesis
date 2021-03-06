package com.engineering.thesis.backend.controllerIntegrationTests;

import com.engineering.thesis.backend.controller.*;
import com.engineering.thesis.backend.controllerIntegrationTests.configration.MockConfiguration;
import com.engineering.thesis.backend.exception.ResourceNotFoundException;
import com.engineering.thesis.backend.model.Patient;
import com.engineering.thesis.backend.serviceImpl.PatientServiceImpl;
import com.engineering.thesis.backend.testObj.Patients;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static com.engineering.thesis.backend.controllerIntegrationTests.role.RoleProcessors.RulesMap;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(value = PatientController.class)
public class PatientEPTests extends MockConfiguration {

    @MockBean
    private PatientServiceImpl patientServiceImpl;

    @Test
    public void getAllShouldReturnPatientsWhenProperRoleIsSelected() {
        System.out.println("Running test -> " + Thread.currentThread().getStackTrace()[1].getMethodName());
        when(patientServiceImpl.getAll()).thenReturn(List.of(Patients.patient1, Patients.patient2));
        RulesMap().forEach((name, Role) -> {
            try {
                System.out.println("Performing getAll patient with role: " + name);
                if (name.equals("Doctor") || name.equals("Manager") || name.equals("Admin")) {
                    this.mockMvc
                            .perform(MockMvcRequestBuilders.get("/api/patients").with(Role))
                            .andExpect(status().isOk())
                            .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2))
                            .andExpect(MockMvcResultMatchers.jsonPath("$[1].insured").value(true))
                            .andExpect(MockMvcResultMatchers.jsonPath("$[0].insured").value(true));
                    System.out.println("All data received properly as expected \n");
                }
                if (name.equals("Invalid") || name.equals("Patient")) {
                    this.mockMvc
                            .perform(MockMvcRequestBuilders.get("/api/patients").with(Role))
                            .andExpect(status().isForbidden());
                    System.out.println("Forbidden status for invalid role as expected \n");
                }
            } catch (Exception e) {
                System.out.println("Unexpected exception during tests \n");
                e.printStackTrace();
            }
        });
    }

    @Test
    public void getByIdShouldReturnPatientWhenProperRoleIsSelected() throws ResourceNotFoundException {
        System.out.println("Running test -> " + Thread.currentThread().getStackTrace()[1].getMethodName());
        final Long id = 1L;
        when(patientServiceImpl.getById(id)).thenReturn(Patients.patient1);
        RulesMap().forEach((name, Role) -> {
            try {
                System.out.println("Performing getById patient with role: " + name);
                if (name.equals("Doctor") || name.equals("Manager") || name.equals("Admin")) {
                    this.mockMvc
                            .perform(MockMvcRequestBuilders.get("/api/patients/1").with(Role)
                                    .accept(MediaType.APPLICATION_JSON))
                            .andExpect(status().isOk())
                            .andExpect(MockMvcResultMatchers.jsonPath("$.insured").value(true));
                    System.out.println("All data received properly as expected \n");
                }
                if (name.equals("Invalid") || name.equals("Patient")) {
                    this.mockMvc
                            .perform(MockMvcRequestBuilders.get("/api/patients/1").with(Role))
                            .andExpect(status().isForbidden());
                    System.out.println("Forbidden status for invalid role as expected \n");
                }
            } catch (Exception e) {
                System.out.println("Unexpected exception during tests \n");
                e.printStackTrace();
            }
        });
    }

    @Test
    public void updatePatientShouldBePossibleWhenProperRoleIsSelected() {
        System.out.println("Running test -> " + Thread.currentThread().getStackTrace()[1].getMethodName());
        RulesMap().forEach((name, Role) -> {
            try {
                System.out.println("Performing update patient with role: " + name);
                if (name.equals("Patient") || name.equals("Manager") || name.equals("Admin")) {
                    this.mockMvc
                            .perform(
                                    put("/api/patients")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content("{\"id\": 1, \"user\": { \"id\": 2}, \"insured\": false}")
                                            .with(Role)
                            )
                            .andExpect(status().isOk());
                    verify(patientServiceImpl, atLeast(1)).update(any(Patient.class));
                    System.out.println("Status 200 received properly as expected \n");
                }
                if (name.equals("Invalid") || name.equals("Doctor")) {
                    this.mockMvc
                            .perform(
                                    put("/api/patients")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content("{\"id\": 1, \"user\": { \"id\": 2}, \"insured\": false}")
                                            .with(Role)
                            )
                            .andExpect(status().isForbidden());
                    System.out.println("Forbidden status for invalid role as expected \n");
                }
            } catch (Exception e) {
                System.out.println("Unexpected exception during tests \n");
                e.printStackTrace();
            }
        });
    }
}