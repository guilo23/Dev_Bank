package com.bia.dev_bank.RepositoryTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.bia.dev_bank.entity.Account;
import com.bia.dev_bank.entity.Card;
import com.bia.dev_bank.entity.CardPayments;
import com.bia.dev_bank.entity.Customer;
import com.bia.dev_bank.entity.enums.CardType;
import com.bia.dev_bank.entity.enums.PayedStatus;
import com.bia.dev_bank.repository.AccountRepository;
import com.bia.dev_bank.repository.CardPaymentsRepository;
import com.bia.dev_bank.repository.CardRepository;
import com.bia.dev_bank.repository.CustomerRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
public class CardPaymentsRepositoryTest {

  @Autowired private CardPaymentsRepository cardPaymentsRepository;

  @Autowired private CardRepository cardRepository;

  @Autowired private CustomerRepository customerRepository;

  @Autowired private AccountRepository accountRepository;

  private Card card;
  private Customer customer;
  private Account account;

  @BeforeEach
  void setUp() {

    customer = new Customer();
    customer.setName("Test Customer");
    customer.setEmail("test@customer.com");
    customer.setPassword("password");
    customer.setBirthday("23-01-2002");
    customer = customerRepository.save(customer);

    account = new Account();
    account.setAccountNumber("123456");
    account.setCustomer(customer);
    account.setCurrentBalance(BigDecimal.valueOf(1000));
    account = accountRepository.save(account);

    card = new Card();
    card.setCardNumber("1234567890123456");
    card.setCardType(CardType.CREDIT);
    card.setCardLimit(BigDecimal.valueOf(10000.0));
    card.setCardBilling(BigDecimal.valueOf(1000.0));
    card.setAccount(account);
    card = cardRepository.save(card);
  }

  @Test
  void whenSaveCardPayment_thenIdIsGeneratedAndFound() {

    CardPayments payment = new CardPayments();
    payment.setCard(card);
    payment.setPaidAmount(BigDecimal.valueOf(150.0));
    payment.setInstallmentAmount(BigDecimal.valueOf(150.0));
    payment.setDueDate(LocalDate.now().plusMonths(1));
    payment.setPaid(PayedStatus.NOT_PAYED);
    payment.setPaymentDate(null);
    payment.setProductName("Test Product");
    payment.setTotalBuying(BigDecimal.valueOf(600.0));

    CardPayments savedPayment = cardPaymentsRepository.save(payment);

    assertNotNull(savedPayment.getId());
    Optional<CardPayments> foundPayment = cardPaymentsRepository.findById(savedPayment.getId());
    assertTrue(foundPayment.isPresent());
    assertThat(foundPayment.get().getProductName()).isEqualTo("Test Product");
  }

  @Test
  void whenDeleteCardPayment_thenItShouldNotBeFound() {

    CardPayments payment = new CardPayments();
    payment.setCard(card);
    payment.setPaidAmount(BigDecimal.valueOf(100.0));
    payment.setInstallmentAmount(BigDecimal.valueOf(100.0));
    payment.setDueDate(LocalDate.now().plusMonths(1));
    payment.setPaid(PayedStatus.NOT_PAYED);
    payment.setPaymentDate(null);
    payment.setProductName("Test Product");
    payment.setTotalBuying(BigDecimal.valueOf(500.0));

    CardPayments savedPayment = cardPaymentsRepository.save(payment);
    assertNotNull(savedPayment.getId());

    cardPaymentsRepository.delete(savedPayment);

    assertTrue(!cardPaymentsRepository.existsById(savedPayment.getId()));
  }
}
