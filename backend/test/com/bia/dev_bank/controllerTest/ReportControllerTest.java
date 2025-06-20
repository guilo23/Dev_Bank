package com.bia.dev_bank.controllerTest;

import com.bia.dev_bank.controller.ReportController;
import com.bia.dev_bank.dto.report.StatementResponse;
import com.bia.dev_bank.security.CustomDetailService;
import com.bia.dev_bank.security.JwtUtil;
import com.bia.dev_bank.service.CardService;
import com.bia.dev_bank.service.ReportService;
import com.bia.dev_bank.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReportController.class)
@ActiveProfiles("test")
public class ReportControllerTest {
  @Autowired private MockMvc mockMvc;

  @MockitoBean
  private JwtUtil jwtUtil;

  @MockitoBean
  private CustomDetailService customDetailService;

  @MockitoBean private ReportService reportService;

  @MockitoBean private TransactionService transactionService;

  @MockitoBean private CardService cardService;

  @Autowired private ObjectMapper objectMapper;

  @Test
  @WithMockUser
  void shouldListAllTransactionByAccountNumber() throws Exception {
    when(transactionService.getStatementByAccountNumber("12345"))
        .thenReturn(
            java.util.List.of(
                new StatementResponse(
                    "transaction", BigDecimal.valueOf(500), LocalDate.now(), "descrition"),
                new StatementResponse(
                    "transaction", BigDecimal.valueOf(500), LocalDate.now(), "descrition")));
    mockMvc
        .perform(get("/bia/report/transaction/12345").with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2));
  }

  @Test
  @WithMockUser
  void shouldListAllcardDebitbyCardNumber() throws Exception {
    when(cardService.cardsDebitPaymentsReport("12345"))
        .thenReturn(
            java.util.List.of(
                new StatementResponse(
                    "debit", BigDecimal.valueOf(500), LocalDate.now(), "descrition"),
                new StatementResponse(
                    "debit", BigDecimal.valueOf(500), LocalDate.now(), "descrition")));
    mockMvc
        .perform(get("/bia/report/debit/12345").with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2));
  }

  @Test
  @WithMockUser
  void shouldListAllReport() throws Exception {
    when(reportService.allcardReports("12345"))
        .thenReturn(
            java.util.List.of(
                new StatementResponse(
                    "credit", BigDecimal.valueOf(500), LocalDate.now(), "descrition"),
                new StatementResponse(
                    "transaction", BigDecimal.valueOf(500), LocalDate.now(), "descrition")));
    mockMvc
        .perform(get("/bia/report/all/12345").with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2));
  }
}
