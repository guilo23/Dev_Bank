package com.bia.dev_bank.controllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bia.dev_bank.controller.CustomerController;
import com.bia.dev_bank.dto.costumer.CustomerRequest;
import com.bia.dev_bank.dto.costumer.CustomerResponse;
import com.bia.dev_bank.dto.costumer.CustomerUpdate;
import com.bia.dev_bank.security.CustomDetailService;
import com.bia.dev_bank.security.JwtUtil;
import com.bia.dev_bank.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CustomerController.class)
@ActiveProfiles("test")
class CustomerControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private CustomDetailService customDetailService;

  @MockitoBean private CustomerService customerService;

  @MockitoBean private JwtUtil jwtUtil;

  @Autowired private ObjectMapper objectMapper;

  @Test
  @WithMockUser
  void shouldCreateCustomerSuccessfully() throws Exception {
    CustomerRequest request =
        new CustomerRequest(
            "Maria", "maria@email.com", "123456", "1990-01-01", "123.456.789-00", "11999999999");
    CustomerResponse response =
        new CustomerResponse(1L, "Maria", "maria@email.com", "123.456.789-00", "11999999999");

    when(customerService.createCustomer(any())).thenReturn(response);

    mockMvc
        .perform(
            post("/bia/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Maria"))
        .andExpect(jsonPath("$.email").value("maria@email.com"));
  }

  @Test
  @WithMockUser
  void shouldGetCustomerById() throws Exception {
    CustomerResponse response =
        new CustomerResponse(1L, "Maria", "maria@email.com", "123.456.789-00", "11999999999");

    when(customerService.getCostumerById(1L)).thenReturn(response);

    mockMvc
        .perform(get("/bia/customer/1").with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Maria"))
        .andExpect(jsonPath("$.email").value("maria@email.com"));
  }

  @Test
  @WithMockUser
  void shouldUpdateCustomer() throws Exception {
    CustomerUpdate update = new CustomerUpdate("new@email.com", "newpass", "1188888888");
    CustomerResponse response =
        new CustomerResponse(1L, "Maria", "new@email.com", "123.456.789-00", "1188888888");

    when(customerService.customerUpdate(Mockito.eq(1L), any())).thenReturn(response);

    mockMvc
        .perform(
            put("/bia/customer/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update))
                .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value("new@email.com"));
  }

  @Test
  @WithMockUser
  void shouldDeleteCustomer() throws Exception {
    mockMvc.perform(delete("/bia/customer/1").with(csrf())).andExpect(status().isNoContent());
  }
}
