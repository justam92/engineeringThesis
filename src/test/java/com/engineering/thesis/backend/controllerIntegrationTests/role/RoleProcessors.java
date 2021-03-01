package com.engineering.thesis.backend.controllerIntegrationTests.role;

import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.Hashtable;

import static com.engineering.thesis.backend.testObj.Users.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

public class RoleProcessors {

    public static RequestPostProcessor DoctorRole() {
        return user(userDoctor1);
    }

    public static RequestPostProcessor PatientRole() {
        return user(userPatient1);
    }

    public static RequestPostProcessor ManagerRole() {
        return user(userManager);
    }

    public static RequestPostProcessor AdminRole() {
        return user(userAdmin);
    }

    public static RequestPostProcessor InvalidRole() {
        return user(userInvalid);
    }

    public static Hashtable<String, RequestPostProcessor> RulesMap() {
        final Hashtable<String, RequestPostProcessor> rulesMap = new Hashtable<String, RequestPostProcessor>();
        rulesMap.put("Doctor", DoctorRole());
        rulesMap.put("Patient", PatientRole());
        rulesMap.put("Manager", ManagerRole());
        rulesMap.put("Admin", AdminRole());
        rulesMap.put("Invalid", InvalidRole());
        return rulesMap;
    }
}
