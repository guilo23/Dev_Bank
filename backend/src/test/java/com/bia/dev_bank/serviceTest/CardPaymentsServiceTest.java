package com.bia.dev_bank.serviceTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.bia.dev_bank.dto.payments.CardPaymentsResponse;
import com.bia.dev_bank.dto.transaction.TransactionResponse;
import com.bia.dev_bank.entity.*;
import com.bia.dev_bank.entity.enums.PayedStatus;
import com.bia.dev_bank.repository.AccountRepository;
import com.bia.dev_bank.repository.CardPaymentsRepository;
import com.bia.dev_bank.repository.TransactionRepository;
import com.bia.dev_bank.service.AccountService;
import com.bia.dev_bank.service.CardPaymentsService;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CardPaymentsServiceTest {

  @Mock private CardPaymentsRepository cardPaymentsRepository;
  @Mock private TransactionRepository transactionRepository;
  @Mock private AccountRepository accountRepository;
  @Mock private AccountService accountService;

  @InjectMocks private CardPaymentsService cardPaymentsService;

  private Account account;
  private Card card;
  private CardPayments payment;

  @BeforeEach
  void setup() {
    account = new Account();
    account.setAccountNumber("000123");
    account.setCustomer(
        new Customer(
            1L, "João", "email", "senha", "1980-01-01", "12345678900", "11999999999", List.of()));
    account.setCurrentBalance(BigDecimal.valueOf(1000));

    card = new Card();
    card.setAccount(account);

    payment = new CardPayments();
    payment.setId(1L);
    payment.setCard(card);
    payment.setInstallmentAmount(BigDecimal.valueOf(500));
    payment.setPaidAmount(BigDecimal.ZERO);
    payment.setTotalBuying(BigDecimal.valueOf(500));
    payment.setDueDate(LocalDate.now());
    payment.setTransactions(new ArrayList<>());
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

    assertEquals(PayedStatus.PAYED, payment.getPAID());
    assertEquals(LocalDate.now(), payment.getPaymentDate());
  }

  @Test
  void shouldSetStatusToPartialWhenPaidLessThanExpected() {
    payment.setPaidAmount(BigDecimal.valueOf(300));
    payment.setInstallmentAmount(BigDecimal.valueOf(500));

    cardPaymentsService.updateCardPaymentStatus(payment);

    assertEquals(PayedStatus.PARTIAL, payment.getPAID());
    assertEquals(LocalDate.now(), payment.getPaymentDate());
  }

  @Test
  void shouldAddTransactionToCardPayments() {
    when(cardPaymentsRepository.findById(1L)).thenReturn(Optional.of(payment));
    when(accountRepository.findByAccountNumber(account.getAccountNumber()))
        .thenReturn(Optional.of(account));
    when(transactionRepository.save(any(Transaction.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    TransactionResponse response = cardPaymentsService.addTransactionToCardPayments(1L);

    assertEquals(payment.getTotalBuying(), response.amount());
    assertEquals("João", response.senderName());
    verify(accountService).debit(account.getAccountNumber(), payment.getTotalBuying());
  }
}
