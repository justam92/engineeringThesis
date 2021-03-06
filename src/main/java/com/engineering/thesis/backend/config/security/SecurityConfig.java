package com.engineering.thesis.backend.config.security;

import com.engineering.thesis.backend.config.jwt.JwtRequestFilter;
import com.engineering.thesis.backend.config.jwt.UnauthorizedHandler;
import com.engineering.thesis.backend.serviceImpl.user.UserDetailsServiceImpl;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

import static com.engineering.thesis.backend.enums.UserRole.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static org.springframework.web.cors.CorsConfiguration.ALL;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@Order(Ordered.HIGHEST_PRECEDENCE)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsServiceImpl userDetailsService;
    private final UnauthorizedHandler unauthorizedHandler;
    private final JwtRequestFilter jwtRequestFilter;
    @Value("${server.host}")
    private String serverHost;
    @Value("${frontend.host}")
    private String frontendHost;
    @Value("${server.port}")
    private String serverPort;
    @Value("${frontend.port}")
    private String frontendPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of(frontendHost+":"+frontendPort, serverHost+":"+serverPort));
        corsConfiguration.addAllowedHeader(ALL);
        corsConfiguration.addAllowedMethod(ALL);

        http.cors().configurationSource(httpServletRequest -> corsConfiguration).and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(STATELESS).and()
                .authorizeRequests()
                .antMatchers("/api", "/api/**").hasAnyRole(DOCTOR.name(), PATIENT.name(), MANAGER.name(), ADMIN.name());

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}