package com.bia.dev_bank.controllerTest;

import com.bia.dev_bank.controller.PaymentController;
import com.bia.dev_bank.dto.payments.CardPaymentsResponse;
import com.bia.dev_bank.entity.LoanPayments;
import com.bia.dev_bank.entity.enums.PayedStatus;
import com.bia.dev_bank.security.CustomDetailService;
import com.bia.dev_bank.security.JwtUtil;
import com.bia.dev_bank.service.CardPaymentsService;
import com.bia.dev_bank.service.LoanPaymentsService;
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
import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
@ActiveProfiles("test")
class PaymentControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private CustomDetailService customDetailService;

    @MockitoBean private LoanPaymentsService loanPaymentsService;

    @MockitoBean private CardPaymentsService cardPaymentsService;

    @Autowired private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void shouldGetLoanPaymentById() throws Exception {
        var loanPaymentId= 1L;
        var response =
                new LoanPayments(
                loanPaymentId, LocalDate.now(), PayedStatus.TO_PAY,BigDecimal.valueOf(1000),BigDecimal.ONE,BigDecimal.ONE,BigDecimal.TEN,null,null,new ArrayList<>());
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
                new CardPaymentsResponse("123456", "geladeira",1,BigDecimal.valueOf(250));

        when(cardPaymentsService.getCardPaymentsById(cardPaymentId)).thenReturn(response);

        mockMvc
                .perform(get("/bia/payments/card/"+ cardPaymentId).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("geladeira"))
                .andExpect(jsonPath("$.cardNumber").value("123456"));
    }
}
