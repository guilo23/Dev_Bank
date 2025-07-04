package com.bia.dev_bank.controllerTest.config;

import com.bia.dev_bank.security.CustomDetailService;
import com.bia.dev_bank.security.JwtAuthFilter;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
public class TestSecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable()) // nova forma de desabilitar o CSRF
        .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
    return http.build();
  }

  @Bean
  public JwtAuthFilter jwtAuthFilter() {
    return Mockito.mock(JwtAuthFilter.class);
  }

  @Bean
  public CustomDetailService customDetailService() {
    return Mockito.mock(CustomDetailService.class);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
