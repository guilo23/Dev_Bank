package com.bia.dev_bank.controllerTest;

import com.bia.dev_bank.controller.LoanController;
import com.bia.dev_bank.dto.loan.LoanRequest;
import com.bia.dev_bank.dto.loan.LoanResponse;
import com.bia.dev_bank.entity.Customer;
import com.bia.dev_bank.entity.enums.LoanType;
import com.bia.dev_bank.security.CustomDetailService;
import com.bia.dev_bank.security.JwtUtil;
import com.bia.dev_bank.service.LoanService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoanController.class)
@ActiveProfiles("test")
class LoanControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean
  private JwtUtil jwtUtil;

  @MockitoBean
  private CustomDetailService customDetailService;

  @MockitoBean private LoanService loanService;

  @Autowired private ObjectMapper objectMapper;

  private Customer originCustomer;
  private Customer destinyCustomer;

  @BeforeEach
  void setup() {
    originCustomer = new Customer();
    originCustomer.setName("Carlos");
  }

  @Test
  @WithMockUser
  void shouldCreateLoan() throws Exception {
    LoanRequest request =
        new LoanRequest(new BigDecimal(10000.0), new BigDecimal(0.1), 12, LoanType.PERSONAL);
    LoanResponse response =
        new LoanResponse(
            originCustomer.getName(), new BigDecimal(10000.0), new BigDecimal(11000.0), 12);

    when(loanService.createLoan(any(), any())).thenReturn(response);

    mockMvc
        .perform(
            post("/bia/loans/1").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.loanAmount").value(10000.0));
  }

  @Test
  @WithMockUser
  void shouldReturnAllLoans() throws Exception {
    LoanResponse response1 =
        new LoanResponse(
            originCustomer.getName(), new BigDecimal(10000.0), new BigDecimal(917.0), 12);
    LoanResponse response2 =
        new LoanResponse(
            originCustomer.getName(), new BigDecimal(20000.0), new BigDecimal(1833.0), 12);

    when(loanService.findAllLoans()).thenReturn(List.of(response1, response2));

    mockMvc
        .perform(get("/bia/loans").with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()").value(2));
  }

  @Test
  @WithMockUser
  void shouldReturnLoanById() throws Exception {
    LoanResponse response =
        new LoanResponse(
            originCustomer.getName(), new BigDecimal(10000.0), new BigDecimal(917.0), 12);

    when(loanService.findLoanById(1L)).thenReturn(response);

    mockMvc
        .perform(get("/bia/loans/1").with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.loanAmount").value(10000.0));
  }
}
