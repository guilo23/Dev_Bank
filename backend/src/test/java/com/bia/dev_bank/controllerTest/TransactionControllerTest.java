package com.bia.dev_bank.controllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bia.dev_bank.controller.TransactionController;
import com.bia.dev_bank.dto.transaction.TransactionRequest;
import com.bia.dev_bank.dto.transaction.TransactionResponse;
import com.bia.dev_bank.service.LoanPaymentsService;
import com.bia.dev_bank.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TransactionController.class)
@ActiveProfiles("test")
class TransactionControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private TransactionService transactionService;

  @MockitoBean private LoanPaymentsService loanPaymentsService;

  @BeforeEach
  public void setup() {}

  @Test
  void shouldCreateTransactionSuccessfully() throws Exception {
    TransactionRequest request = new TransactionRequest(BigDecimal.valueOf(100.0), "456");
    TransactionResponse response =
        new TransactionResponse(BigDecimal.valueOf(100.0), "Maria", "joão", LocalDate.now());

    when(transactionService.createTransaction(any(TransactionRequest.class), eq("123")))
        .thenReturn(response);

    mockMvc
        .perform(
            post("/bia/transactions/123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(
            content()
                .string(
                    org.hamcrest.Matchers.containsString("Parabéns sua transferência para Maria")));
  }

  @Test
  void shouldGetTransactionById() throws Exception {
    TransactionResponse response =
        new TransactionResponse(BigDecimal.valueOf(200.0), "João", "Maria", LocalDate.now());

    when(transactionService.getTransactionById(1L)).thenReturn(response);

    mockMvc
        .perform(get("/bia/transactions/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.amount").value(200.0))
        .andExpect(jsonPath("$.receiverName").value("João"));
  }

  @Test
  void shouldGetTransactionsByAccountNumber() throws Exception {
    List<TransactionResponse> responses =
        List.of(
            new TransactionResponse(BigDecimal.valueOf(50.0), "Maria", "João", LocalDate.now()));

    when(transactionService.getTransactionByAccountNumber("123")).thenReturn(responses);

    mockMvc
        .perform(get("/bia/transactions/account/123"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].amount").value(50.0));
  }

  @Test
  void shouldGetAllTransactions() throws Exception {
    List<TransactionResponse> responses =
        List.of(
            new TransactionResponse(BigDecimal.valueOf(75.0), "Carlos", "João", LocalDate.now()));

    when(transactionService.getAllTransactions()).thenReturn(responses);

    mockMvc
        .perform(get("/bia/transactions/list"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].receiverName").value("Carlos"));
  }

  @Test
  void shouldDeleteTransaction() throws Exception {
    mockMvc
        .perform(delete("/bia/transactions/1"))
        .andExpect(status().isOk())
        .andExpect(
            content()
                .string(
                    org.hamcrest.Matchers.containsString("Transação foi excluida com sucesso")));
  }

  @Test
  void shouldAddTransactionToLoanPayments() throws Exception {
    Long loanPaymentsId = 5L;
    TransactionRequest request = new TransactionRequest(BigDecimal.valueOf(90.0), "456");
    System.out.println(objectMapper.writeValueAsString(request));

    mockMvc
        .perform(
            post("/bia/transactions/loanPayments/5")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andDo(result -> System.out.println("Response status: " + result.getResponse().getStatus()))
        .andDo(
            result ->
                System.out.println("Response body: " + result.getResponse().getContentAsString()))
        .andExpect(status().isOk())
        .andExpect(content().string("Pago com sucesso"));
  }
}
