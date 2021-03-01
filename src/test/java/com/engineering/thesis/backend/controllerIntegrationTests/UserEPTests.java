package com.engineering.thesis.backend.controllerIntegrationTests;

import com.engineering.thesis.backend.controller.UserController;
import com.engineering.thesis.backend.controllerIntegrationTests.configration.MockConfiguration;
import com.engineering.thesis.backend.exception.ResourceNotFoundException;
import com.engineering.thesis.backend.repository.UserRepository;
import com.engineering.thesis.backend.serviceImpl.user.UserServiceImpl;
import com.engineering.thesis.backend.testObj.Users;
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
import static java.util.Optional.ofNullable;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(value = UserController.class)
class UserEPTests extends MockConfiguration {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserServiceImpl userServiceImpl;

    @Test
    public void getAllShouldReturnUsersWhenProperRoleIsSelected() {
        System.out.println("Running test -> " + Thread.currentThread().getStackTrace()[1].getMethodName());
        when(userServiceImpl.getAll()).thenReturn(List.of(Users.userDoctor1, Users.userDoctor2));
        RulesMap().forEach((name, Role) -> {
            try {
                System.out.println("Performing getAll users with role: " + name);
                if (name.equals("Admin")) {
                    this.mockMvc
                            .perform(MockMvcRequestBuilders.get("/api/users").with(Role))
                            .andExpect(status().isOk())
                            .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2))
                            .andExpect(MockMvcResultMatchers.jsonPath("$[1].surname").value("Kowalsky"))
                            .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Tom"))
                            .andExpect(MockMvcResultMatchers.jsonPath("$[0].surname").value("Kowalsky"))
                            .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Tom"));
                    System.out.println("All data received properly as expected \n");
                }
                if (name.equals("Invalid") || name.equals("Patient") || name.equals("Manager") || name.equals("Doctor")) {
                    this.mockMvc
                            .perform(MockMvcRequestBuilders.get("/api/users").with(Role))
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
    public void getByIdShouldReturnUserWhenProperRoleIsSelected() throws ResourceNotFoundException {
        System.out.println("Running test -> " + Thread.currentThread().getStackTrace()[1].getMethodName());
        final Long id = 1L;
        when(userServiceImpl.getById(id)).thenReturn(ofNullable(Users.userDoctor1));
        RulesMap().forEach((name, Role) -> {
            try {
                System.out.println("Performing getById with role: " + name);
                if (name.equals("Admin")) {
                    this.mockMvc
                            .perform(get("/api/users/" + id).with(Role)
                                    .accept(MediaType.APPLICATION_JSON))
                            .andExpect(status().isOk())
                            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Tom"))
                            .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value("Kowalsky"));
                    System.out.println("All data received properly as expected \n");
                }
                if (name.equals("Invalid") || name.equals("Patient") || name.equals("Manager") || name.equals("Doctor")) {
                    this.mockMvc
                            .perform(get("/api/users/" + id).with(Role))
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
    public void deactivateByEmailUserShouldBePossibleWhenProperRoleIsSelected() {
        System.out.println("Running test -> " + Thread.currentThread().getStackTrace()[1].getMethodName());
        final String email = "randomemail@random.com";
        RulesMap().forEach((name, Role) -> {
            try {
                System.out.println("Performing delete user with role: " + name);
                if (name.equals("Patient") || name.equals("Doctor")) {
                    this.mockMvc
                            .perform(delete("/api/users/" + email +"/deactivate").with(Role))
                            .andExpect(status().isBadRequest());
                    System.out.println("Status 400 received properly as expected \n");
                }
                if (name.equals("Admin") || name.equals("Manager")) {
                    this.mockMvc
                            .perform(delete("/api/users/" + email +"/deactivate").with(Role))
                            .andExpect(status().isOk());
                    System.out.println("Status 200 received properly as expected \n");
                }
                if (name.equals("Invalid")) {
                    this.mockMvc
                            .perform(delete("/api/users/" + email +"/deactivate").with(Role))
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