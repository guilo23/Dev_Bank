package com.bia.dev_bank.controllerTest;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bia.dev_bank.controller.*;
import com.bia.dev_bank.dto.card.*;
import com.bia.dev_bank.entity.enums.*;
import com.bia.dev_bank.security.*;
import com.bia.dev_bank.service.*;
import com.fasterxml.jackson.databind.*;
import java.math.*;
import java.util.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.http.*;
import org.springframework.security.test.context.support.*;
import org.springframework.test.context.*;
import org.springframework.test.context.bean.override.mockito.*;
import org.springframework.test.web.servlet.*;

@WebMvcTest(CardController.class)
@ActiveProfiles("test")
class CardControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private JwtUtil jwtUtil;

  @MockitoBean private CustomDetailService customDetailService;

  @MockitoBean private CardService cardService;

  @MockitoBean private CardPaymentsService cardPaymentsService;

  @Autowired private ObjectMapper objectMapper;

  @Test
  @WithMockUser
  void shouldCreateCard() throws Exception {
    CreditRequest request = new CreditRequest(CardType.DEBIT, "1111222233334444", BigDecimal.ZERO);
    CardResponse response =
        new CardResponse(1L, CardType.DEBIT, "1111222233334444", BigDecimal.ZERO, "Maria");

    Mockito.when(cardService.cardCreate(any(CreditRequest.class), eq("123456")))
        .thenReturn(response);

    mockMvc
        .perform(
            post("/bia/cards/add/123456")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(csrf()))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.cardNumber").value("1111222233334444"));
  }

  @Test
  @WithMockUser
  void shouldGetCardById() throws Exception {
    CardResponse response =
        new CardResponse(1L, CardType.DEBIT, "1111222233334444", BigDecimal.ZERO, "Maria");

    Mockito.when(cardService.getCardById(1L)).thenReturn(response);

    mockMvc
        .perform(get("/bia/cards/1").with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.cardNumber").value("1111222233334444"));
  }

  @Test
  @WithMockUser
  void shouldGetAllCardsByAccount() throws Exception {
    CardResponse response =
        new CardResponse(1L, CardType.DEBIT, "1111222233334444", BigDecimal.ZERO, "Maria");

    Mockito.when(cardService.getAllCardByAccountNumber("123456")).thenReturn(List.of(response));

    mockMvc
        .perform(get("/bia/cards/list/123456").with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].cardNumber").value("1111222233334444"));
  }

  @Test
  @WithMockUser
  void shouldUpdateCard() throws Exception {
    CreditUpdate update = new CreditUpdate(BigDecimal.valueOf(2000));
    CardResponse response =
        new CardResponse(1L, CardType.DEBIT, "1111222233334444", BigDecimal.valueOf(2000), "Maria");
    Mockito.when(cardService.cardUpdate(any(CreditUpdate.class), eq(1L))).thenReturn(response);

    mockMvc
        .perform(
            put("/bia/cards/1")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsString(update)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.cardLimit").value(2000));
  }

  @Test
  @WithMockUser
  void shouldDeleteCard() throws Exception {
    mockMvc
        .perform(delete("/bia/cards/1").with(csrf()))
        .andExpect(status().isOk())
        .andExpect(content().string("card has been deleted"));

    Mockito.verify(cardService).cardDelete(1L);
  }
}
