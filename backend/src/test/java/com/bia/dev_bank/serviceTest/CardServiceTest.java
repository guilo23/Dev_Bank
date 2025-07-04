package com.bia.dev_bank.serviceTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.bia.dev_bank.dto.card.CardResponse;
import com.bia.dev_bank.dto.card.CreditRequest;
import com.bia.dev_bank.dto.card.CreditUpdate;
import com.bia.dev_bank.dto.payments.CardPaymentsRequest;
import com.bia.dev_bank.dto.payments.CardPaymentsResponse;
import com.bia.dev_bank.dto.report.StatementResponse;
import com.bia.dev_bank.entity.*;
import com.bia.dev_bank.entity.enums.AccountType;
import com.bia.dev_bank.entity.enums.CardType;
import com.bia.dev_bank.entity.enums.PayedStatus;
import com.bia.dev_bank.repository.AccountRepository;
import com.bia.dev_bank.repository.CardPaymentsRepository;
import com.bia.dev_bank.repository.CardRepository;
import com.bia.dev_bank.service.CardService;
import com.bia.dev_bank.utils.SecurityUtil;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class CardServiceTest {

  @InjectMocks private CardService cardService;

  @Mock private CardRepository cardRepository;

  @Mock private CardPaymentsRepository cardPaymentsRepository;

  @Mock private SecurityUtil securityUtil;

  @Mock private AccountRepository accountRepository;

  private Account account;
  private Card card;
  private Customer customer;

  @BeforeEach
  void setup() {
    customer =
        new Customer(
            1L,
            "João da Silva",
            "joao@email.com",
            "senha123",
            "USER",
            "1985-01-01",
            "111.222.333-44",
            "11999999999",
            List.of());

    account =
        new Account(
            "123456",
            customer,
            AccountType.CHECKING,
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>(),
            BigDecimal.valueOf(500.0),
            LocalDate.now());

    card =
        new Card(
            1L,
            "1111222233334444",
            CardType.DEBIT,
            BigDecimal.ZERO,
            BigDecimal.ZERO,
            new ArrayList<>(),
            new ArrayList<>(),
            account);
  }

  @Test
  void shouldCreateDebitCardSuccessfully() {
    CreditRequest request = new CreditRequest(CardType.DEBIT, "1111222233334444", BigDecimal.ZERO);

    when(securityUtil.getCurrentUserId()).thenReturn(1L);
    when(accountRepository.findByAccountNumber("123456")).thenReturn(Optional.of(account));
    when(cardRepository.save(any(Card.class))).thenAnswer(i -> i.getArguments()[0]);

    CardResponse response = cardService.cardCreate(request, "123456");

    assertEquals("1111222233334444", response.cardNumber());
    assertEquals(BigDecimal.valueOf(500.0), response.cardLimit());
    verify(cardRepository).save(any(Card.class));
  }

  @Test
  void shouldCreateCreditCardSuccessfully() {
    CreditRequest request =
        new CreditRequest(CardType.CREDIT, "5555666677778888", BigDecimal.valueOf(2000));

    when(securityUtil.getCurrentUserId()).thenReturn(1L);
    when(accountRepository.findByAccountNumber("123456")).thenReturn(Optional.of(account));
    when(cardRepository.save(any(Card.class))).thenAnswer(i -> i.getArguments()[0]);

    CardResponse response = cardService.cardCreate(request, "123456");

    assertEquals("5555666677778888", response.cardNumber());
    assertEquals(BigDecimal.valueOf(2000), response.cardLimit());
    verify(cardRepository).save(any(Card.class));
  }

  @Test
  void shouldAddCreditCardPaymentSuccessfully() {
    Card card = new Card();
    card.setCardType(CardType.CREDIT);
    card.setPurchases(new ArrayList<>());
    card.setCardNumber("123456");
    Account acc = new Account();
    acc.setCustomer(customer);
    card.setAccount(acc);

    CardPaymentsRequest request =
        new CardPaymentsRequest("123456", "Notebook", new BigDecimal("3000.00"), 3);

    when(securityUtil.getCurrentUserId()).thenReturn(1L);
    when(cardRepository.findCardByCardNumber("123456")).thenReturn(Optional.of(card));
    when(accountRepository.findByAccountNumber(null)).thenReturn(Optional.of(acc));

    CardPaymentsResponse response = cardService.addCreditCardPayment(request);

    assertEquals("123456", response.cardNumber());
    assertEquals("Notebook", response.productName());
    assertEquals(3, response.installmentNumber());
    assertNotNull(response.installmentAmount());
    verify(cardPaymentsRepository, times(1)).saveAll(anyList());
  }

  @Test
  void shouldReturnDebitPaymentsReport() {
    Card card = new Card();
    card.setId(1L);
    card.setCardType(CardType.DEBIT);
    card.setCardNumber("111222");
    Account acc = new Account();
    acc.setCustomer(customer);
    card.setAccount(acc);

    List<CardPayments> payments =
        List.of(
            new CardPayments(
                1L,
                "Produto A",
                new BigDecimal("100.00"),
                1,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                LocalDate.now(),
                LocalDate.now(),
                PayedStatus.TO_PAY,
                card,
                new ArrayList<>()),
            new CardPayments(
                2L,
                "Produto B",
                new BigDecimal("200.00"),
                1,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                LocalDate.now(),
                LocalDate.now(),
                PayedStatus.TO_PAY,
                card,
                new ArrayList<>()));

    when(securityUtil.getCurrentUserId()).thenReturn(1L);
    when(cardRepository.findCardByCardNumber("111222")).thenReturn(Optional.of(card));
    when(accountRepository.findByAccountNumber(null)).thenReturn(Optional.of(acc));
    when(cardPaymentsRepository.findByCardId(1L)).thenReturn(payments);

    List<StatementResponse> result = cardService.cardsDebitPaymentsReport("111222");

    assertEquals(2, result.size());
    assertTrue(result.get(0).description().contains("Produto A"));
  }

  @Test
  void shouldReturnCreditPaymentsReport() {
    Card card = new Card();
    card.setCardType(CardType.CREDIT);
    card.setCardNumber("999888");
    Account acc = new Account();
    acc.setCustomer(customer);
    card.setAccount(acc);

    CreditPurchase purchase =
        new CreditPurchase(null, "Teclado", new BigDecimal("150.00"), LocalDate.now(), 2, card);
    card.setPurchases(List.of(purchase));

    when(securityUtil.getCurrentUserId()).thenReturn(1L);
    when(cardRepository.findCardByCardNumber("999888")).thenReturn(Optional.of(card));
    when(accountRepository.findByAccountNumber(null)).thenReturn(Optional.of(acc));

    List<StatementResponse> result = cardService.cardsCreditPaymentsReport("999888");

    assertEquals(1, result.size());
    assertEquals("one Teclado", result.get(0).description().split("of ")[1]);
  }

  @Test
  void shouldThrowExceptionWhenAccountNotFoundOnCreate() {
    CreditRequest request = new CreditRequest(CardType.DEBIT, "0000111122223333", BigDecimal.ZERO);
    when(securityUtil.getCurrentUserId()).thenReturn(1L);
    when(accountRepository.findByAccountNumber("000000")).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> cardService.cardCreate(request, "000000"));
  }

  @Test
  void shouldReturnCardById() {
    when(securityUtil.getCurrentUserId()).thenReturn(1L);
    when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
    when(accountRepository.findByAccountNumber("123456")).thenReturn(Optional.of(account));

    CardResponse response = cardService.getCardById(1L);

    assertEquals("1111222233334444", response.cardNumber());
  }

  @Test
  void shouldThrowExceptionWhenCardNotFoundById() {
    when(cardRepository.findById(99L)).thenReturn(Optional.empty());

    assertThrows(NoSuchElementException.class, () -> cardService.getCardById(99L));
  }

  @Test
  void shouldReturnCardsByAccountNumber() {
    when(securityUtil.getCurrentUserId()).thenReturn(1L);
    when(accountRepository.findByAccountNumber("123456")).thenReturn(Optional.of(account));
    when(cardRepository.findAllCardsByAccountAccountNumber("123456"))
        .thenReturn(Optional.of(List.of(card)));

    List<CardResponse> responses = cardService.getAllCardByAccountNumber("123456");

    assertEquals(1, responses.size());
    assertEquals("1111222233334444", responses.get(0).cardNumber());
  }

  @Test
  void shouldReturnCardsForReport() {
    when(cardRepository.findAllCardsByAccountAccountNumber("123456"))
        .thenReturn(Optional.of(List.of(card)));

    List<Card> responses = cardService.getAllCardForReport("123456");

    assertEquals(1, responses.size());
    assertEquals("1111222233334444", responses.get(0).getCardNumber());
  }

  @Test
  void shouldUpdateCardLimitSuccessfully() {
    when(securityUtil.getCurrentUserId()).thenReturn(1L);
    when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
    when(accountRepository.findByAccountNumber("123456")).thenReturn(Optional.of(account));

    CreditUpdate update = new CreditUpdate(BigDecimal.valueOf(5000));
    CardResponse response = cardService.cardUpdate(update, 1L);

    assertEquals(BigDecimal.valueOf(5000), response.cardLimit());
  }

  @Test
  void shouldDeleteCardById() {
    when(securityUtil.getCurrentUserId()).thenReturn(1L);
    when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
    when(accountRepository.findByAccountNumber("123456")).thenReturn(Optional.of(account));

    cardService.cardDelete(1L);

    verify(cardRepository).deleteById(1L);
  }

  @Test
  void shouldThrowExceptionWhenDeletingNonExistentCard() {
    when(cardRepository.findById(2L)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> cardService.cardDelete(2L));
  }
}
