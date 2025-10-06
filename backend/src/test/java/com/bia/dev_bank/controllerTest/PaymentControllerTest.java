package com.bia.dev_bank.controllerTest;

import com.bia.dev_bank.controller.*;
import com.bia.dev_bank.dto.payments.*;
import com.bia.dev_bank.entity.*;
import com.bia.dev_bank.entity.enums.*;
import com.bia.dev_bank.repository.*;
import com.bia.dev_bank.security.*;
import com.bia.dev_bank.service.*;
import com.fasterxml.jackson.databind.*;
import java.math.*;
import java.time.*;
import java.util.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.security.test.context.support.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import org.springframework.test.context.*;
import org.springframework.test.context.bean.override.mockito.*;
import org.springframework.test.web.servlet.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
@ActiveProfiles("test")
class PaymentControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private JwtUtil jwtUtil;

  @MockitoBean private CustomDetailService customDetailService;

  @MockitoBean private LoanPaymentsService loanPaymentsService;

  @MockitoBean private CardPaymentsService cardPaymentsService;

  @MockitoBean
  private CardPaymentsRepository cardPaymentsRepository;


  @Autowired private ObjectMapper objectMapper;

  @Test
  @WithMockUser
  void shouldGetLoanPaymentById() throws Exception {
    var loanPaymentId = 1L;
    var response =
        new LoanPayments(
            loanPaymentId,
            LocalDate.now(),
            PayedStatus.TO_PAY,
            BigDecimal.valueOf(1000),
            BigDecimal.ONE,
            BigDecimal.ONE,
            BigDecimal.TEN,
            null,
            null,
            new ArrayList<>());
    when(loanPaymentsService.getLoanPaymentsById(loanPaymentId)).thenReturn(response);
    mockMvc
        .perform(get("/bia/payments/loan").param("loanPaymentId", "1").with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.paymentAmount").value(1000))
        .andExpect(jsonPath("$.interestAmount").value(BigDecimal.ONE));
  }

  @Test
  @WithMockUser
  void shouldGetCardPaymentById() throws Exception {
    var cardPaymentId = 1L;
    CardPaymentsResponse response =
        new CardPaymentsResponse(
            "123456", "geladeira", 1, BigDecimal.valueOf(250), LocalDate.now());

    when(cardPaymentsService.getCardPaymentsById(cardPaymentId)).thenReturn(response);

    mockMvc
        .perform(get("/bia/payments/card/" + cardPaymentId).with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.productName").value("geladeira"))
        .andExpect(jsonPath("$.cardNumber").value("123456"));
  }

  @Test
  @WithMockUser
  void shouldGetCardPaymentsInstallments() throws Exception {
    var cardId = 1L;
    var response =
        java.util.List.of(
            new CardPaymentsResponse(
                "123456", "geladeira", 1, BigDecimal.valueOf(250), LocalDate.now()),
            new CardPaymentsResponse(
                "123456", "geladeira", 2, BigDecimal.valueOf(250), LocalDate.now()));

    when(cardPaymentsService.getCardPaymentsreportByid(cardId)).thenReturn(response);

    mockMvc
        .perform(get("/bia/payments/card-payments").param("cardId", "1").with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2));
  }
}
