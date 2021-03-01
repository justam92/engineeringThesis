package com.engineering.thesis.backend.controllerIntegrationTests;

import com.engineering.thesis.backend.controller.AppointmentController;
import com.engineering.thesis.backend.controllerIntegrationTests.configration.MockConfiguration;
import com.engineering.thesis.backend.exception.ResourceNotFoundException;
import com.engineering.thesis.backend.model.Appointment;
import com.engineering.thesis.backend.model.authentication.AppointmentRequest;
import com.engineering.thesis.backend.serviceImpl.AppointmentServiceImpl;
import com.engineering.thesis.backend.testObj.Appointments;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(value = AppointmentController.class)
class AppointmentEPTests extends MockConfiguration {

    @MockBean
    private AppointmentServiceImpl appointmentServiceImpl;

    @Test
    public void getAllShouldReturnAppointmentWhenProperRoleIsSelected() {
        System.out.println("Running test -> " + Thread.currentThread().getStackTrace()[1].getMethodName());
        when(appointmentServiceImpl.getAll()).thenReturn(List.of(Appointments.appointment1, Appointments.appointment2));
        RulesMap().forEach((name, Role) -> {
            try {
                System.out.println("Performing getAll appointment with role: " + name);
                if (name.equals("Doctor") || name.equals("Patient") || name.equals("Manager") || name.equals("Admin")) {
                    this.mockMvc
                            .perform(MockMvcRequestBuilders.get("/api/appointments").with(Role))
                            .andExpect(status().isOk())
                            .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2))
                            .andExpect(MockMvcResultMatchers.jsonPath("$[1].cost").value(1000))
                            .andExpect(MockMvcResultMatchers.jsonPath("$[0].cost").value(1000));
                    System.out.println("All data received properly as expected \n");
                }
                if (name.equals("Invalid")) {
                    this.mockMvc
                            .perform(MockMvcRequestBuilders.get("/api/appointments").with(Role))
                            .andExpect(status().isForbidden());
                    System.out.println("Forbidden status for invalid role as expected \n");
                }
            } catch (Exception e) {
                System.out.println("Unexpected exception during tests: \n");
                e.printStackTrace();
            }
        });
    }

    @Test
    public void getByIdShouldReturnAppointmentWhenProperRoleIsSelected() throws ResourceNotFoundException {
        System.out.println("Running test -> " + Thread.currentThread().getStackTrace()[1].getMethodName());
        final Long id = 1L;
        when(appointmentServiceImpl.getById(id)).thenReturn(java.util.Optional.of(Appointments.appointment1));
        RulesMap().forEach((name, Role) -> {
            try {
                System.out.println("Performing getById appointment with role: " + name);
                if (name.equals("Doctor") || name.equals("Patient") || name.equals("Manager") || name.equals("Admin")) {
                    this.mockMvc
                            .perform(MockMvcRequestBuilders.get("/api/appointments/1").with(Role))
                            .andExpect(status().isOk())
                            .andExpect(MockMvcResultMatchers.jsonPath("$.cost").value(1000))
                            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L));
                    System.out.println("All data received properly as expected \n");
                }
                if (name.equals("Invalid")) {
                    this.mockMvc
                            .perform(MockMvcRequestBuilders.get("/api/appointments/1").with(Role))
                            .andExpect(status().isForbidden());
                    System.out.println("Forbidden status for invalid role as expected \n");
                }
            } catch (Exception e) {
                System.out.println("Unexpected exception during tests: \n");
                e.printStackTrace();
            }
        });
    }

    @Test
    public void createAppointmentShouldBePossibleWhenProperRoleIsSelected() {
        System.out.println("Running test -> " + Thread.currentThread().getStackTrace()[1].getMethodName());
        RulesMap().forEach((name, Role) -> {
            try {
                System.out.println("Performing create appointment with role: " + name);
                if (name.equals("Doctor") || name.equals("Patient") || name.equals("Manager") || name.equals("Admin")) {
                    this.mockMvc
                            .perform(
                                    post("/api/appointments")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content("{\"id\": 1, \"appointmentDate\":\"2014-04-28T16:00:49.455\", \"cost\":1 , \"patient\": {\"id\": 1}, \"doctor\": {\"id\": 1} }")
                                            .with(Role)
                            )
                            .andExpect(status().isOk());
                    verify(appointmentServiceImpl, atLeast(1)).create(any(AppointmentRequest.class));
                    System.out.println("All data received properly as expected \n");

                }
                if (name.equals("Invalid")) {
                    this.mockMvc
                            .perform(
                                    post("/api/appointments")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content("{\"id\": 3, \"appointmentDate\":\"2014-04-28T16:00:49.455\", \"cost\":1 , \"patient\": {\"id\": 1}, \"doctor\": {\"id\": 1} }")
                                            .with(Role)
                            )
                            .andExpect(status().isForbidden());
                    System.out.println("Forbidden status for invalid role as expected \n");
                }
            } catch (Exception e) {
                System.out.println("Unexpected exception during tests: \n");
                e.printStackTrace();
            }
        });
    }

    @Test
    public void deleteByIdAppointmentShouldBePossibleWhenProperRoleIsSelected() {
        System.out.println("Running test -> " + Thread.currentThread().getStackTrace()[1].getMethodName());
        RulesMap().forEach((name, Role) -> {
            try {
                System.out.println("Performing deleteById appointment with role: " + name);
                if (name.equals("Doctor") || name.equals("Patient") || name.equals("Manager") || name.equals("Admin")) {
                    this.mockMvc
                            .perform(delete("/api/appointments/1").with(Role))
                            .andExpect(status().isOk());
                    System.out.println("All data received properly as expected \n");
                }
                if (name.equals("Invalid")) {
                    this.mockMvc
                            .perform(delete("/api/appointments/1").with(Role))
                            .andExpect(status().isForbidden());
                    System.out.println("Forbidden status for invalid role as expected \n");
                }
            } catch (Exception e) {
                System.out.println("Unexpected exception during tests: \n");
                e.printStackTrace();
            }
        });
    }

    @Test
    public void updateByIdAppointmentShouldBePossibleWhenProperRoleIsSelected() {
        System.out.println("Running test -> " + Thread.currentThread().getStackTrace()[1].getMethodName());
        RulesMap().forEach((name, Role) -> {
            try {
                System.out.println("Performing updateById appointment with role: " + name);
                if (name.equals("Doctor") || name.equals("Patient") || name.equals("Manager") || name.equals("Admin")) {
                    this.mockMvc
                            .perform(
                                    put("/api/appointments/1")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content("{\"id\": 1, \"appointmentDate\":\"2014-04-28T16:00:49.455\", \"cost\":1 , \"patient\": {\"id\": 1}, \"doctor\": {\"id\": 1} }")
                                            .with(Role)
                            )
                            .andExpect(status().isOk());
                    verify(appointmentServiceImpl, atLeast(1)).updateById(anyLong(), any(Appointment.class));
                    System.out.println("All data received properly as expected \n");
                }
                if (name.equals("Invalid")) {
                    this.mockMvc
                            .perform(
                                    put("/api/appointments/1")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content("{\"id\": 2, \"appointmentDate\":\"2014-04-28T16:00:49.455\", \"cost\":1 , \"patient\": {\"id\": 1}, \"doctor\": {\"id\": 1} }")
                                            .with(Role)
                            )
                            .andExpect(status().isForbidden());
                    System.out.println("Forbidden status for invalid role as expected \n");
                }
            } catch (Exception e) {
                System.out.println("Unexpected exception during tests: \n");
                e.printStackTrace();
            }
        });
    }
}