package com.bia.dev_bank.controllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.bia.dev_bank.controller.AccountController;
import com.bia.dev_bank.dto.account.AccountRequest;
import com.bia.dev_bank.dto.account.AccountResponse;
import com.bia.dev_bank.dto.account.AccountUpdate;
import com.bia.dev_bank.entity.enums.AccountType;
import com.bia.dev_bank.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AccountController.class)
@ActiveProfiles
public class AccountControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private AccountService accountService;

  @Autowired private ObjectMapper objectMapper;

  @Test
  void shouldCreateAccountSuccessfully() throws Exception {
    Long customerId = 1L;
    var request = new AccountRequest(AccountType.CHECKING, BigDecimal.valueOf(500.0));
    var response =
        new AccountResponse("12345678-9", "Maria", AccountType.CHECKING, BigDecimal.valueOf(500.0));

    when(accountService.createAccount(any(AccountRequest.class), eq(customerId)))
        .thenReturn(response);

    mockMvc
        .perform(
            post("/bia/account/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(content().string("12345678-9 congratulation your account has been created"));
  }

  @Test
  void shouldMakeDepositSuccessfully() throws Exception {
    String accountNumber = "12345678-9";
    var update = new AccountUpdate(AccountType.CHECKING, BigDecimal.valueOf(50.0));
    var response =
        new AccountResponse("12345678-9", "Maria", AccountType.CHECKING, BigDecimal.valueOf(550.0));

    when(accountService.accountDeposit(any(AccountUpdate.class), eq(accountNumber)))
        .thenReturn(response);

    mockMvc
        .perform(
            post("/bia/account/balanceIn/12345678-9")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)))
        .andExpect(status().isAccepted())
        .andExpect(content().string("the value 50.0 has been deposited in your account "));
  }

  @Test
  void shouldMakeCashOutSuccessfully() throws Exception {
    String accountNumber = "12345678-9";
    var update = new AccountUpdate(AccountType.CHECKING, BigDecimal.valueOf(50.0));
    var response =
        new AccountResponse("12345678-9", "Maria", AccountType.CHECKING, BigDecimal.valueOf(550.0));

    when(accountService.accountCashOut(any(AccountUpdate.class), eq(accountNumber)))
        .thenReturn(response);

    mockMvc
        .perform(
            post("/bia/account/balanceOut/12345678-9")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)))
        .andExpect(status().isAccepted())
        .andExpect(content().string("the value 50.0 has been debit of your account"));
  }

  @Test
  void shouldGetAccountByAccountNumber() throws Exception {
    var response =
        new AccountResponse("12345678-9", "Maria", AccountType.CHECKING, BigDecimal.valueOf(500));

    when(accountService.getAccountById("12345678-9")).thenReturn(response);

    mockMvc
        .perform(get("/bia/account/12345678-9"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accountNumber").value("12345678-9"))
        .andExpect(jsonPath("$.customerName").value("Maria"));
  }

  @Test
  void shouldListAllAccountsByCustomerId() throws Exception {
    when(accountService.getAllAccountByCostumerId(1L))
        .thenReturn(
            java.util.List.of(
                new AccountResponse("123", "Maria", AccountType.SAVINGS, BigDecimal.valueOf(500.0)),
                new AccountResponse(
                    "456", "Maria", AccountType.CHECKING, BigDecimal.valueOf(1500.0))));
    mockMvc
        .perform(get("/bia/account/list/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2));
  }

  @Test
  void ShouldUpdateAccount() throws Exception {
    var update = new AccountUpdate(AccountType.CHECKING, BigDecimal.valueOf(1500));

    doNothing().when(accountService).accountUpdate(eq("12345678-9"), any(AccountUpdate.class));
    mockMvc
        .perform(
            put("/bia/account/12345678-9")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)))
        .andExpect(status().isOk())
        .andExpect(content().string("your account has been updated"));
  }

  @Test
  void ShouldDeleteAccount() throws Exception {
    doNothing().when(accountService).accountDelete("12345678-9");

    mockMvc
        .perform(delete("/bia/account/12345678-9"))
        .andExpect(status().isOk())
        .andExpect(content().string(":( your account has been deleted"));
  }
}
