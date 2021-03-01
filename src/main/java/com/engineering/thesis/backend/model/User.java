package com.engineering.thesis.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collections;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users", schema = "healthcare")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @NotBlank(message = "Name can't be empty")
    private String name;
    @NotBlank(message = "Surname can't be empty")
    private String surname;
    @NotBlank(message = "Email can't be empty")
    @Email(message = "Invalid email address")
    private String email;
    @NotBlank(message = "Password can't be empty")
    private String password;
    @NotBlank(message = "Role can't be empty")
    private String role;
    @ManyToOne
    @JoinColumn(name = "medical_facility_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "Fk_medical_facility"))
    @NotNull(message = "Medical Facility can't be empty")
    private MedicalFacility medicalFacility;

    private boolean isActive;
    private boolean isEmailVerified;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}