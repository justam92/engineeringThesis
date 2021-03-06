package com.engineering.thesis.backend.controllerIntegrationTests;

import com.engineering.thesis.backend.controller.PatientMedicalDataController;
import com.engineering.thesis.backend.controllerIntegrationTests.configration.MockConfiguration;
import com.engineering.thesis.backend.exception.ResourceNotFoundException;
import com.engineering.thesis.backend.model.PatientMedicalData;
import com.engineering.thesis.backend.model.authentication.PatientMedicalDataRequest;
import com.engineering.thesis.backend.serviceImpl.PatientMedicalDataServiceImpl;
import com.engineering.thesis.backend.testObj.PatientMedicalDatas;
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
@WebMvcTest(value = PatientMedicalDataController.class)
public class PatientMedDataEPTests extends MockConfiguration {

    @MockBean
    private PatientMedicalDataServiceImpl patientMedicalDataServiceImpl;

    @Test
    public void getAllShouldReturnPatientMedDataWhenProperRoleIsSelected() {
        System.out.println("Running test -> " + Thread.currentThread().getStackTrace()[1].getMethodName());
        when(patientMedicalDataServiceImpl.getAll()).thenReturn(List.of(PatientMedicalDatas.patientMedicalData1, PatientMedicalDatas.patientMedicalData1));
        RulesMap().forEach((name, Role) -> {
            try {
                System.out.println("Performing getAll patientMedData with role: " + name);
                if (name.equals("Doctor") || name.equals("Patient") || name.equals("Manager") || name.equals("Admin")) {
                    this.mockMvc
                            .perform(MockMvcRequestBuilders.get("/api/patientsMedicalData").with(Role))
                            .andExpect(status().isOk())
                            .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2));
                    System.out.println("All data received properly as expected \n");
                }
                if (name.equals("Invalid")) {
                    this.mockMvc
                            .perform(MockMvcRequestBuilders.get("/api/patientsMedicalData").with(Role))
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
    public void getByIdShouldReturnPatientMedDataWhenProperRoleIsSelected() throws ResourceNotFoundException {
        System.out.println("Running test -> " + Thread.currentThread().getStackTrace()[1].getMethodName());
        final Long id = 1L;
        when(patientMedicalDataServiceImpl.getById(id)).thenReturn(java.util.Optional.of(PatientMedicalDatas.patientMedicalData1));
        RulesMap().forEach((name, Role) -> {
            try {
                System.out.println("Performing getById patientMedData with role: " + name);
                if (name.equals("Doctor") || name.equals("Patient") || name.equals("Manager") || name.equals("Admin")) {
                    this.mockMvc
                            .perform(MockMvcRequestBuilders.get("/api/patientsMedicalData/1").with(Role))
                            .andExpect(status().isOk())
                            .andExpect(MockMvcResultMatchers.jsonPath("$.medicalProcedure").value("Biopsy"))
                            .andExpect(MockMvcResultMatchers.jsonPath("$.additionalNotes").value("Funny patient LOL like him tho"));
                    System.out.println("All data received properly as expected \n");
                }
                if (name.equals("Invalid")) {
                    this.mockMvc
                            .perform(MockMvcRequestBuilders.get("/api/patientsMedicalData/1").with(Role))
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
    public void createPatientMedDataShouldBePossibleWhenProperRoleIsSelected() {
        System.out.println("Running test -> " + Thread.currentThread().getStackTrace()[1].getMethodName());
        RulesMap().forEach((name, Role) -> {
            try {
                System.out.println("Performing create patientMedicalData with role: " + name);
                if (name.equals("Doctor") || name.equals("Manager") || name.equals("Admin")) {
                    this.mockMvc
                            .perform(
                                    post("/api/patientsMedicalData")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content("{ \"id\": 1, \"patient\": { \"id\": 18 }, \"doctor\": { \"id\": 19 }, \"treatmentDate\": \"2000-12-26T00:00:00\", \"medicalProcedure\": \"Cataract surgery\", \"additionalNotes\": \"didnt pay in time\" }")
                                            .with(Role)
                            )
                            .andExpect(status().isOk());
                    verify(patientMedicalDataServiceImpl, atLeast(1)).create(any(PatientMedicalDataRequest.class));
                    System.out.println("Status 200 received properly as expected \n");
                }
                if (name.equals("Invalid") || name.equals("Patient")) {
                    this.mockMvc
                            .perform(
                                    post("/api/patientsMedicalData")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content("{ \"id\": 2, \"patient\": { \"id\": 18 }, \"doctor\": { \"id\": 19 }, \"treatmentDate\": \"2000-12-26T00:00:00\", \"medicalProcedure\": \"Cataract surgery\", \"additionalNotes\": \"didnt pay in time\" }")
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

    @Test
    public void deleteByIdPatientMedDataShouldBePossibleWhenProperRoleIsSelected() {
        System.out.println("Running test -> " + Thread.currentThread().getStackTrace()[1].getMethodName());
        RulesMap().forEach((name, Role) -> {
            try {
                System.out.println("Performing deleteById PatientMedData with role: " + name);
                if (name.equals("Doctor") || name.equals("Manager") || name.equals("Admin")) {
                    this.mockMvc
                            .perform(delete("/api/patientsMedicalData/1").with(Role))
                            .andExpect(status().isOk());
                    System.out.println("Status 200 received properly as expected \n");
                }
                if (name.equals("Invalid") || name.equals("Patient")) {
                    this.mockMvc
                            .perform(delete("/api/patientsMedicalData/1").with(Role))
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
    public void updateByIdPatientMedDataShouldBePossibleWhenProperRoleIsSelected() {
        System.out.println("Running test -> " + Thread.currentThread().getStackTrace()[1].getMethodName());
        RulesMap().forEach((name, Role) -> {
            try {
                System.out.println("Performing updateById patientMedicalData with role: " + name);
                if (name.equals("Doctor") || name.equals("Manager") || name.equals("Admin")) {
                    this.mockMvc
                            .perform(
                                    put("/api/patientsMedicalData/1")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content("{ \"id\": 1, \"patient\": { \"id\": 18 }, \"doctor\": { \"id\": 19 }, \"treatmentDate\": \"2000-12-26T00:00:00\", \"medicalProcedure\": \"Cataract surgery\", \"additionalNotes\": \"didnt pay in time\" }")
                                            .with(Role)
                            )
                            .andExpect(status().isOk());
                    verify(patientMedicalDataServiceImpl, atLeast(1)).updateById(anyLong(), any(PatientMedicalData.class));
                    System.out.println("Status 200 received properly as expected \n");
                }
                if (name.equals("Invalid") || name.equals("Patient")) {
                    this.mockMvc
                            .perform(
                                    put("/api/patientsMedicalData/1")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content("{ \"id\": 1, \"patient\": { \"id\": 18 }, \"doctor\": { \"id\": 19 }, \"treatmentDate\": \"2000-12-26T00:00:00\", \"medicalProcedure\": \"Cataract surgery\", \"additionalNotes\": \"didnt pay in time\" }")
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