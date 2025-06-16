package com.bia.dev_bank.dto.loanDTOs;

import com.bia.dev_bank.entity.Loan;
import java.math.BigDecimal;

public record LoanResponse(
    String customerName, BigDecimal loanAmount, BigDecimal monthlyInstallment, int installments) {
  public LoanResponse(Loan loan) {
    this(
        loan.getCustomer().getName(),
        loan.getLoanAmount(),
        loan.getMonthlyInstallment(),
        loan.getInstallments());
  }
}
