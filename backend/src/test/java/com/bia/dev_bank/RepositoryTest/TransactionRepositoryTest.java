package com.bia.dev_bank.RepositoryTest;

import static org.junit.jupiter.api.Assertions.*;

import com.bia.dev_bank.entity.Account;
import com.bia.dev_bank.entity.Customer;
import com.bia.dev_bank.entity.Transaction;
import com.bia.dev_bank.repository.AccountRepository;
import com.bia.dev_bank.repository.CustomerRepository;
import com.bia.dev_bank.repository.TransactionRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
public class TransactionRepositoryTest {
  @Autowired private AccountRepository accountRepository;

  @Autowired private TransactionRepository transactionRepository;
  @Autowired private CustomerRepository customerRepository;

  private Account originAccount;
  private Account destinyAccount;
  private Customer destinyCustomer;
  private Customer originCustomer;

  @BeforeEach
  void setup() {
    originCustomer = new Customer();
    originCustomer.setName("Carlos");

    destinyCustomer = new Customer();
    destinyCustomer.setName("Maria");

    originAccount = new Account();
    originAccount.setAccountNumber("123");
    originAccount.setCustomer(originCustomer);

    destinyAccount = new Account();
    destinyAccount.setAccountNumber("456");
    destinyAccount.setCustomer(destinyCustomer);
  }

  @Test
  void shouldfindAndSaveAccount() {
    var transaction =
        new Transaction(
            null,
            BigDecimal.valueOf(100.00),
            originAccount,
            destinyAccount,
            null,
            null,
            LocalDate.now());
    var saved = transactionRepository.save(transaction);
    assertNotNull(saved.getId());
    var found = transactionRepository.findById(transaction.getId()).orElse(null);
    assertNotNull(found);
    assertEquals("Carlos", found.getDestinyAccount().getCustomer().getName());
    assertEquals(BigDecimal.valueOf(100.00), found.getAmount());
    assertEquals("Maria", found.getOriginAccount().getCustomer().getName());
  }

  @Test
  void ShouldDeleteAccount() {
    var transaction =
        new Transaction(
            null,
            BigDecimal.valueOf(100.00),
            originAccount,
            destinyAccount,
            null,
            null,
            LocalDate.now());
    var saved = transactionRepository.save(transaction);
    transactionRepository.delete(transaction);
    assertTrue(transactionRepository.findById(transaction.getId()).isEmpty());
  }
}
