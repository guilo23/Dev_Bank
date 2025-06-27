package com.bia.dev_bank.RepositoryTest;

import static org.junit.jupiter.api.Assertions.*;

import com.bia.dev_bank.entity.Account;
import com.bia.dev_bank.entity.Customer;
import com.bia.dev_bank.entity.enums.AccountType;
import com.bia.dev_bank.repository.AccountRepository;
import com.bia.dev_bank.repository.CustomerRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
public class AccountRepositoryTest {

  @Autowired private AccountRepository accountRepository;
  @Autowired private CustomerRepository customerRepository;

  private Customer customer;

  @BeforeEach
  void setup() {
    customer =
        new Customer(
            null,
            "João da Silva",
            "joao@email.com",
            "senha123",
            "1985-01-01",
            "111.222.333-44",
            "11999999999",
            "USER",
            List.of());
    customer = customerRepository.save(customer);
  }

  @Test
  void shouldfindAndSaveAccount() {
    var account =
        new Account(
            "12345678-9",
            customer,
            AccountType.CHECKING,
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>(),
            BigDecimal.valueOf(500.0),
            LocalDate.now());
    var saved = accountRepository.save(account);
    assertNotNull(saved.getAccountNumber());
    var found = accountRepository.findByAccountNumber(account.getAccountNumber()).orElse(null);
    assertNotNull(found);
    assertEquals("João da Silva", found.getCustomer().getName());
  }

  @Test
  void ShouldDeleteAccount() {
    var account =
        new Account(
            "12345678-9",
            customer,
            AccountType.CHECKING,
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>(),
            BigDecimal.valueOf(500.0),
            LocalDate.now());
    var saved = accountRepository.save(account);
    accountRepository.delete(account);

    assertTrue(accountRepository.findByAccountNumber(account.getAccountNumber()).isEmpty());
  }
}
