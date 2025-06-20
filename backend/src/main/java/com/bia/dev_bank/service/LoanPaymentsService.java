package com.bia.dev_bank.service;

import com.bia.dev_bank.dto.transaction.TransactionRequest;
import com.bia.dev_bank.dto.transaction.TransactionResponse;
import com.bia.dev_bank.entity.LoanPayments;
import com.bia.dev_bank.entity.Transaction;
import com.bia.dev_bank.entity.enums.PayedStatus;
import com.bia.dev_bank.repository.AccountRepository;
import com.bia.dev_bank.repository.LoanPaymentsRepository;
import com.bia.dev_bank.repository.TransactionRepository;
import com.bia.dev_bank.utils.SecurityUtil;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoanPaymentsService {
  private final LoanPaymentsRepository loanPaymentsRepository;
  private final TransactionRepository transactionRepository;
  private final AccountRepository accountRepository;
  public final SecurityUtil securityUtil;

  public LoanPayments getLoanPaymentsById(Long id) {
    var custumerId = securityUtil.getCurrentUserId();
    var loan =
        loanPaymentsRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("loan not found"));
    if (!loan.getLoan().getCustomer().getId().equals(custumerId)) {
      throw new AccessDeniedException("permission denied");
    }
    return loanPaymentsRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("report not found"));
  }

  public LoanPayments updatePaidAmount(Long loanPaymentId) {
    LoanPayments payment = getLoanPaymentsById(loanPaymentId);

    BigDecimal totalPaid =
        payment.getTransactions().stream()
            .map(t -> t.getAmount() != null ? t.getAmount() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    payment.setPaidAmount(totalPaid);
    updateLoanPaymentStatus(payment);
    return loanPaymentsRepository.save(payment);
  }

  public TransactionResponse addTransactionToLoanPayment(
      Long loanPaymentId, TransactionRequest request) {
    var custumerId = securityUtil.getCurrentUserId();
    var loan =
        loanPaymentsRepository
            .findById(loanPaymentId)
            .orElseThrow(() -> new EntityNotFoundException("loan not found"));
    if (!loan.getLoan().getCustomer().getId().equals(custumerId)) {
      throw new AccessDeniedException("permission denied");
    }
    var payment = getLoanPaymentsById(loanPaymentId);
    var account =
        accountRepository
            .findByAccountNumber(request.destinyAccountNumber())
            .orElseThrow(() -> new EntityNotFoundException("not found"));
    var transaction =
        new Transaction(null, request.amount(), null, account, null, null, LocalDate.now());
    transaction.setLoanPayment(payment);
    transaction.setTransactionDate(LocalDate.now());

    Transaction saved = transactionRepository.save(transaction);
    updatePaidAmount(loanPaymentId);

    return new TransactionResponse(saved);
  }

  public void updateLoanPaymentStatus(LoanPayments payment) {
    Double expected =
        Double.parseDouble(
            payment.getPaymentAmount() != null ? String.valueOf(payment.getPaymentAmount()) : "0");
    Double paid =
        Double.parseDouble(
            payment.getPaidAmount() != null ? String.valueOf(payment.getPaidAmount()) : "0");

    LocalDate today = LocalDate.now();
    LocalDate scheduledDate = payment.getScheduledPaymentDate();

    if (paid >= expected) {
      payment.setPayedStatus(PayedStatus.PAYED);
      payment.setPaidDate(today);
    } else if (paid > 0) {
      payment.setPayedStatus(PayedStatus.PARTIAL);
      payment.setPaidDate(today);
    } else if (today.isAfter(scheduledDate)) {
      payment.setPayedStatus(PayedStatus.TO_PAY);
    } else {
      payment.setPayedStatus(PayedStatus.TO_PAY);
    }
  }
}
