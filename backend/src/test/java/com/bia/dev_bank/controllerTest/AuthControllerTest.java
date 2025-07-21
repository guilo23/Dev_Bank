package com.bia.dev_bank.controllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bia.dev_bank.dto.costumer.AuthRequest;
import com.bia.dev_bank.security.CustomDetailService;
import com.bia.dev_bank.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private PasswordEncoder passwordEncoder;
  @MockitoBean private CustomDetailService customDetailService;
  @MockitoBean private AuthenticationManager authenticationManager;
  @MockitoBean private JwtUtil jwtUtil;
  @MockitoBean private com.bia.dev_bank.service.CustomerService customerService;

  @Test
  void shouldReturnJwtTokenWhenLoginSuccess() throws Exception {
    AuthRequest request = new AuthRequest("user@email.com", "123456");

    var userDetails =
        new User(
            request.email(),
            passwordEncoder.encode(request.password()),
            List.of(new SimpleGrantedAuthority("USER")));

    var authentication =
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

    Mockito.when(authenticationManager.authenticate(any())).thenReturn(authentication);
    Mockito.when(jwtUtil.generateToken(userDetails)).thenReturn("fake-jwt-token");

    mockMvc
        .perform(
            post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").value("fake-jwt-token"));
  }

  @Test
  void shouldRegisterUserSuccessfully() throws Exception {
    com.bia.dev_bank.dto.costumer.CustomerRequest request =
        new com.bia.dev_bank.dto.costumer.CustomerRequest(
            "Bia", "bia@email.com", "123456", "2004-01-01", "123.456.789-01", "11999999999");

    com.bia.dev_bank.dto.costumer.CustomerResponse response =
        new com.bia.dev_bank.dto.costumer.CustomerResponse(
            2L, "Bia", "bia@email.com", "123.456.789-01", "11999999999");

    Mockito.when(customerService.createCustomer(any())).thenReturn(response);

    mockMvc
        .perform(
            post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Bia"))
        .andExpect(jsonPath("$.email").value("bia@email.com"));
  }
}
