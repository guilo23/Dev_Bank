package com.bia.dev_bank.RepositoryTest;

import com.bia.dev_bank.entity.Customer;
import com.bia.dev_bank.entity.Loan;
import com.bia.dev_bank.entity.LoanPayments;
import com.bia.dev_bank.entity.enums.LoanType;
import com.bia.dev_bank.entity.enums.PayedStatus;
import com.bia.dev_bank.repository.CustomerRepository;
import com.bia.dev_bank.repository.LoanPaymentsRepository;
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
public class LoanPaymentRepositoryTest {
  @Autowired private LoanPaymentsRepository loanPaymentsRepository;

  @Autowired private CustomerRepository customerRepository;

  @Autowired private LoanRepository loanRepository;

  @Autowired private LoanPaymentsRepository loanPaymentsRepositoryy;

  @Test
  void shouldSaveLoan() {
    Customer customer =
        new Customer(
            null, "Ana", "ana@mail.com", "1234", "1990-01-01", "11111111111", "11999999999","", null);
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
    var loanPayments =
        new LoanPayments(
            null,
            LocalDate.now(),
            PayedStatus.TO_PAY,
            new BigDecimal(500),
            new BigDecimal(500),
            new BigDecimal(0.2),
            new BigDecimal(500),
            null,
            loan,
            List.of());

    var saves = loanPaymentsRepository.save(loanPayments);

    assertNotNull(saves.getLoanPaymentId());
    assertNotNull(saves.getPaymentAmount());
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
    var loanPayments =
        new LoanPayments(
            null,
            LocalDate.now(),
            PayedStatus.TO_PAY,
            new BigDecimal(500),
            new BigDecimal(500),
            new BigDecimal(0.2),
            new BigDecimal(500),
            null,
            loan,
            List.of());

    var saves = loanPaymentsRepository.save(loanPayments);

    loanPaymentsRepository.delete(loanPayments);

    assertTrue(loanPaymentsRepository.findById(loanPayments.getLoanPaymentId()).isEmpty());
  }
}
