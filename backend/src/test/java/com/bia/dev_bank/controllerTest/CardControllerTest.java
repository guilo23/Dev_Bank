package com.bia.dev_bank.controllerTest;

import com.bia.dev_bank.controller.CardController;
import com.bia.dev_bank.dto.cardDTOs.CardRequest;
import com.bia.dev_bank.dto.cardDTOs.CardResponse;
import com.bia.dev_bank.dto.cardDTOs.CardUpdate;
import com.bia.dev_bank.entity.enums.CardType;
import com.bia.dev_bank.service.CardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CardController.class)
@ActiveProfiles("test")
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CardService cardService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateCard() throws Exception {
        CardRequest request = new CardRequest(CardType.DEBIT, "1111222233334444", BigDecimal.ZERO);
        CardResponse response = new CardResponse("1111222233334444",BigDecimal.ZERO,"Maria");

        Mockito.when(cardService.cardCreate(any(CardRequest.class), eq("123456"))).thenReturn(response);

        mockMvc.perform(post("/bia/cards/123456")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cardNumber").value("1111222233334444"));
    }

    @Test
    void shouldGetCardById() throws Exception {
        CardResponse response = new CardResponse("1111222233334444",BigDecimal.ZERO,"Maria");

        Mockito.when(cardService.getCardById(1L)).thenReturn(response);

        mockMvc.perform(get("/bia/cards/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardNumber").value("1111222233334444"));
    }

    @Test
    void shouldGetAllCardsByAccount() throws Exception {
        CardResponse response = new CardResponse("1111222233334444",BigDecimal.ZERO,"Maria");

        Mockito.when(cardService.getAllCardByAccountNumber("123456")).thenReturn(List.of(response));

        mockMvc.perform(get("/bia/cards/list/123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cardNumber").value("1111222233334444"));
    }

    @Test
    void shouldUpdateCard() throws Exception {
        CardUpdate update = new CardUpdate(BigDecimal.valueOf(2000));
        CardResponse response = new CardResponse("1111222233334444",BigDecimal.valueOf(2000),"Maria");

        Mockito.when(cardService.cardUpdate(any(CardUpdate.class), eq(1L))).thenReturn(response);

        mockMvc.perform(put("/bia/cards/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardLimit").value(2000));
    }

    @Test
    void shouldDeleteCard() throws Exception {
        mockMvc.perform(delete("/bia/cards/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("cartão deletado com sucesso"));

        Mockito.verify(cardService).cardDelete(1L);
    }
}

