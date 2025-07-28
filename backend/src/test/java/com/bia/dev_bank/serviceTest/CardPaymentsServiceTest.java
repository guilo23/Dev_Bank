package com.bia.dev_bank.serviceTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.bia.dev_bank.dto.payments.*;
import com.bia.dev_bank.dto.transaction.*;
import com.bia.dev_bank.entity.*;
import com.bia.dev_bank.entity.enums.*;
import com.bia.dev_bank.repository.*;
import com.bia.dev_bank.service.*;
import com.bia.dev_bank.utils.*;
import jakarta.persistence.*;
import java.math.*;
import java.time.*;
import java.util.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

@ExtendWith(MockitoExtension.class)
class CardPaymentsServiceTest {

  @Mock private CardPaymentsRepository cardPaymentsRepository;
  @Mock private TransactionRepository transactionRepository;
  @Mock private AccountRepository accountRepository;
  @Mock private AccountService accountService;
  @Mock private SecurityUtil securityUtil;

  @InjectMocks private CardPaymentsService cardPaymentsService;

  private Account account;
  private Card card;
  private CardPayments payment;

  @BeforeEach
  void setup() {
    account = new Account();
    // ...
    account.setCurrentBalance(BigDecimal.valueOf(1000));

    card = new Card();
    account = new Account();
    account.setAccountNumber("000123");
    account.setCustomer(
        new Customer(
            1L,
            "João da Silva",
            "joao@email.com",
            "senha123",
            "USER",
            "1985-01-01",
            "111.222.333-44",
            "11999999999",
            List.of()));
    card.setCardBilling(BigDecimal.valueOf(500));
    card.setAccount(account);
    payment = new CardPayments();
    payment.setId(1L);
    payment.setCard(card);
  }

  @Test
  void shouldReturnCardPaymentById() {
    when(cardPaymentsRepository.findById(1L)).thenReturn(Optional.of(payment));

    CardPaymentsResponse response = cardPaymentsService.getCardPaymentsById(1L);

    assertEquals(payment.getCard().getCardNumber(), response.cardNumber());
  }

  @Test
  void shouldThrowWhenCardPaymentNotFound() {
    when(cardPaymentsRepository.findById(999L)).thenReturn(Optional.empty());

    assertThrows(
        EntityNotFoundException.class, () -> cardPaymentsService.getCardPaymentsById(999L));
  }

  @Test
  void shouldUpdatePaidAmountCorrectly() {
    Transaction tx = new Transaction();
    tx.setAmount(BigDecimal.valueOf(300));
    payment.getTransactions().add(tx);

    when(cardPaymentsRepository.findById(1L)).thenReturn(Optional.of(payment));

    cardPaymentsService.updatePaidAmount(1L);

    assertEquals(BigDecimal.valueOf(300), payment.getPaidAmount());
    verify(cardPaymentsRepository).save(payment);
  }

  @Test
  void shouldSetStatusToPayedWhenPaidEqualsExpected() {
    payment.setPaidAmount(BigDecimal.valueOf(500));
    payment.setInstallmentAmount(BigDecimal.valueOf(500));

    cardPaymentsService.updateCardPaymentStatus(payment);

    assertEquals(PayedStatus.PAYED, payment.getPaid());
    assertEquals(LocalDate.now(), payment.getPaymentDate());
  }

  @Test
  void shouldSetStatusToPartialWhenPaidLessThanExpected() {
    payment.setPaidAmount(BigDecimal.valueOf(300));
    payment.setInstallmentAmount(BigDecimal.valueOf(500));

    cardPaymentsService.updateCardPaymentStatus(payment);

    assertEquals(PayedStatus.PARTIAL, payment.getPaid());
    assertEquals(LocalDate.now(), payment.getPaymentDate());
  }

  @Test
  void shouldAddTransactionToCardPayments() {
    when(cardPaymentsRepository.findById(1L)).thenReturn(Optional.of(payment));
    when(accountRepository.findByAccountNumber(account.getAccountNumber()))
        .thenReturn(Optional.of(account));
    when(transactionRepository.save(any(Transaction.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
    when(securityUtil.getCurrentUserId()).thenReturn(1L);

    TransactionResponse response =
        cardPaymentsService.addTransactionToCardPayments(1L, BigDecimal.valueOf(100));

    assertEquals(BigDecimal.valueOf(100), response.amount());
    assertEquals("João da Silva", response.senderName());
    verify(accountService).debit(account.getAccountNumber(), response.amount());
  }
}
