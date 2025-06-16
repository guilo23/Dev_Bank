package com.bia.dev_bank.dto.loanDTOs;

import com.bia.dev_bank.entity.LoanPayments;
import com.bia.dev_bank.entity.enums.PayedStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanPaymentsResponse(
    BigDecimal LoanAmount,
    LocalDate scheduledPaymentDate,
    PayedStatus payedStatus,
    BigDecimal paymentAmount,
    BigDecimal principalAmount,
    BigDecimal interestAmount,
    BigDecimal paidAmount,
    LocalDate paidDate) {
  public LoanPaymentsResponse(LoanPayments loanPayments) {
    this(
        loanPayments.getLoan().getLoanAmount(),
        loanPayments.getScheduledPaymentDate(),
        loanPayments.getPayedStatus(),
        loanPayments.getPaymentAmount(),
        loanPayments.getPrincipalAmount(),
        loanPayments.getInterestAmount(),
        loanPayments.getPaidAmount(),
        loanPayments.getPaidDate());
  }
}
