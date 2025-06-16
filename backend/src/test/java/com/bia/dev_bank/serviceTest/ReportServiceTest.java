package com.bia.dev_bank.serviceTest;

import com.bia.dev_bank.dto.reportDTOs.StatementResponse;
import com.bia.dev_bank.entity.Card;
import com.bia.dev_bank.entity.enums.CardType;
import com.bia.dev_bank.service.CardService;
import com.bia.dev_bank.service.ReportService;
import com.bia.dev_bank.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {
    @InjectMocks
    private ReportService reportService;

    @Mock
    private CardService cardService;

    @Mock
    private TransactionService transactionService;

    @Test
    void shouldReturnAllStatementsForAccount() {
        String accountNumber = "000001";


        Card debitCard = new Card();
        debitCard.setCardNumber("DEBIT123");
        debitCard.setCardType(CardType.DEBIT);

        Card creditCard = new Card();
        creditCard.setCardNumber("CREDIT123");
        creditCard.setCardType(CardType.CREDIT);

        when(cardService.getAllCardForReport(accountNumber)).thenReturn(List.of(debitCard, creditCard));


        StatementResponse debitResponse = new StatementResponse(
                "Pagamento no débito",
                new BigDecimal("100.00"),
                LocalDate.now(),
                "Compra de supermercado"
        );
        when(cardService.cardsDebitPaymentsReport("DEBIT123")).thenReturn(List.of(debitResponse));


        StatementResponse creditResponse = new StatementResponse(
                "Pagamento no crédito",
                new BigDecimal("300.00"),
                LocalDate.now(),
                "Compra parcelada"
        );
        when(cardService.cardsCreditPaymentsReport("CREDIT123")).thenReturn(List.of(creditResponse));


        StatementResponse txResponse = new StatementResponse(
                "Transferência",
                new BigDecimal("500.00"),
                LocalDate.now(),
                "Transferência para João"
        );
        when(transactionService.getStatementByAccountNumber(accountNumber)).thenReturn(List.of(txResponse));


        List<StatementResponse> result = reportService.allcardReports(accountNumber);


        assertEquals(3, result.size());
        assertTrue(result.contains(debitResponse));
        assertTrue(result.contains(creditResponse));
        assertTrue(result.contains(txResponse));

        verify(cardService).getAllCardForReport(accountNumber);
        verify(cardService).cardsDebitPaymentsReport("DEBIT123");
        verify(cardService).cardsCreditPaymentsReport("CREDIT123");
        verify(transactionService).getStatementByAccountNumber(accountNumber);
    }
}
