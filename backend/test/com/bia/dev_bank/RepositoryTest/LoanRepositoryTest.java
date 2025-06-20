package com.bia.dev_bank.RepositoryTest;

import com.bia.dev_bank.entity.Customer;
import com.bia.dev_bank.entity.Loan;
import com.bia.dev_bank.entity.enums.LoanType;
import com.bia.dev_bank.repository.CustomerRepository;
import com.bia.dev_bank.repository.LoanRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
public class LoanRepositoryTest {

  @Autowired private LoanRepository loanRepository;

  @Autowired private CustomerRepository customerRepository;

  @Test
  void shouldSaveLoan() {
    Customer customer =
        new Customer(
            null, "Ana", "ana@mail.com", "1234","USER", "1990-01-01", "11111111111", "11999999999", null);
    customer = customerRepository.save(customer);

    Loan loan =
        new Loan(
            null,
            LoanType.PERSONAL,
            LocalDate.now(),
            new BigDecimal(10000.0),
            10,
            new BigDecimal(0.0),
            new BigDecimal(0.0),
            new BigDecimal(0.1),
            customer,
            List.of());
    loan.calculatedLoanDetails();

    Loan saved = loanRepository.save(loan);

    assertNotNull(saved.getId());
    assertNotNull(saved.getTotalPayable());
  }

  @Test
  void shouldDeleteLoan() {
    Customer customer =
        new Customer(
            null, "Ana", "ana@mail.com", "1234","USER", "1990-01-01", "11111111111", "11999999999", null);
    customer = customerRepository.save(customer);

    Loan loan =
        new Loan(
            null,
            LoanType.PERSONAL,
            LocalDate.now(),
            new BigDecimal(10000.0),
            10,
            new BigDecimal(0.0),
            new BigDecimal(0.0),
            new BigDecimal(0.1),
            customer,
            List.of());
    loan.calculatedLoanDetails();

    Loan saved = loanRepository.save(loan);
    loanRepository.delete(loan);
    assertTrue(loanRepository.findById(loan.getId()).isEmpty());
  }
}
