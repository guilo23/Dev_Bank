package com.bia.dev_bank.controllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bia.dev_bank.controller.CardController;
import com.bia.dev_bank.dto.card.CardResponse;
import com.bia.dev_bank.dto.card.CreditRequest;
import com.bia.dev_bank.dto.card.CreditUpdate;
import com.bia.dev_bank.entity.enums.CardType;
import com.bia.dev_bank.service.CardPaymentsService;
import com.bia.dev_bank.service.CardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CardController.class)
@ActiveProfiles("test")
class CardControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private CardService cardService;

  @MockitoBean private CardPaymentsService cardPaymentsService;

  @Autowired private ObjectMapper objectMapper;

  @Test
  void shouldCreateCard() throws Exception {
    CreditRequest request = new CreditRequest(CardType.DEBIT, "1111222233334444", BigDecimal.ZERO);
    CardResponse response = new CardResponse("1111222233334444", BigDecimal.ZERO, "Maria");

    Mockito.when(cardService.cardCreate(any(CreditRequest.class), eq("123456")))
        .thenReturn(response);

    mockMvc
        .perform(
            post("/bia/cards/add/123456")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.cardNumber").value("1111222233334444"));
  }

  @Test
  void shouldGetCardById() throws Exception {
    CardResponse response = new CardResponse("1111222233334444", BigDecimal.ZERO, "Maria");

    Mockito.when(cardService.getCardById(1L)).thenReturn(response);

    mockMvc
        .perform(get("/bia/cards/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.cardNumber").value("1111222233334444"));
  }

  @Test
  void shouldGetAllCardsByAccount() throws Exception {
    CardResponse response = new CardResponse("1111222233334444", BigDecimal.ZERO, "Maria");

    Mockito.when(cardService.getAllCardByAccountNumber("123456")).thenReturn(List.of(response));

    mockMvc
        .perform(get("/bia/cards/list/123456"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].cardNumber").value("1111222233334444"));
  }

  @Test
  void shouldUpdateCard() throws Exception {
    CreditUpdate update = new CreditUpdate(BigDecimal.valueOf(2000));
    CardResponse response = new CardResponse("1111222233334444", BigDecimal.valueOf(2000), "Maria");

    Mockito.when(cardService.cardUpdate(any(CreditUpdate.class), eq(1L))).thenReturn(response);

    mockMvc
        .perform(
            put("/bia/cards/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.cardLimit").value(2000));
  }

  @Test
  void shouldDeleteCard() throws Exception {
    mockMvc
        .perform(delete("/bia/cards/1"))
        .andExpect(status().isOk())
        .andExpect(content().string("card has been deleted"));

    Mockito.verify(cardService).cardDelete(1L);
  }
}
