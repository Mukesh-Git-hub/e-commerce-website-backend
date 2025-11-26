package com.example.savvyproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable()) // disable CSRF to allow Postman requests
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/users/register").permitAll() // public endpoint
                    .requestMatchers("/api/users/login").permitAll() // will be login endpoint later
                    .anyRequest().authenticated() // secure all other routes
            )
            .formLogin(form -> form.disable()) // disable Spring default login UI
            .httpBasic(basic -> basic.disable()); // disable basic auth popup

        return http.build();
    }
}
