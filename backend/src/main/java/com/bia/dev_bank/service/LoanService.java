package com.bia.dev_bank.service;

import com.bia.dev_bank.dto.loan.LoanRequest;
import com.bia.dev_bank.dto.loan.LoanResponse;
import com.bia.dev_bank.entity.Customer;
import com.bia.dev_bank.entity.Loan;
import com.bia.dev_bank.entity.LoanPayments;
import com.bia.dev_bank.entity.enums.PayedStatus;
import com.bia.dev_bank.repository.CustomerRepository;
import com.bia.dev_bank.repository.LoanPaymentsRepository;
import com.bia.dev_bank.repository.LoanRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoanService {

  private final LoanRepository loanRepository;
  private final CustomerRepository customerRepository;
  private final LoanPaymentsRepository loanPaymentsRepository;

  public LoanResponse createLoan(LoanRequest request, Long customerId) {

    Customer customer =
        customerRepository
            .findById(customerId)
            .orElseThrow(
                () -> new RuntimeException("Cliente não encontrado com ID: " + customerId));

    var loan =
        new Loan(
            null,
            request.loanType(),
            LocalDate.now(),
            request.loanAmount(),
            request.installments(),
            null,
            null,
            request.interestRate(),
            customer,
            List.of());
    loan.calculatedLoanDetails();

    loanRepository.save(loan);
    generatedLoanPayments(loan);

    return new LoanResponse(loan);
  }

  public void generatedLoanPayments(Loan loan) {
    int term = loan.getInstallments();
    BigDecimal totalAmount = loan.getLoanAmount();
    BigDecimal monthlyAmount =
        totalAmount.divide(BigDecimal.valueOf(term), 2, RoundingMode.HALF_UP);

    List<LoanPayments> payments = new ArrayList<>();

    LocalDate currentDate = loan.getStartDate();

    for (int i = 0; i < term; i++) {
      LoanPayments payment = new LoanPayments();
      payment.setLoan(loan);
      payment.setInterestAmount(BigDecimal.valueOf(0.2));
      payment.setScheduledPaymentDate(currentDate.plusMonths(i));
      payment.setPaymentAmount(
          monthlyAmount.add(monthlyAmount.multiply(payment.getInterestAmount())));
      payment.setPrincipalAmount(monthlyAmount);
      payment.setPayedStatus(PayedStatus.TO_PAY);
      payment.setPaidAmount(BigDecimal.ZERO);
      payments.add(payment);
    }

    loanPaymentsRepository.saveAll(payments);
  }

  public List<LoanResponse> findAllLoans() {
    var loans = loanRepository.findAll();

    return loans.stream().map(LoanResponse::new).collect(Collectors.toList());
  }

  public LoanResponse findLoanById(Long id) {
    var loan =
        loanRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("no loan for this id"));
    return new LoanResponse(loan);
  }
}
