package com.bia.dev_bank.dto.loanDTOs;

import com.bia.dev_bank.entity.enums.LoanType;
import java.math.BigDecimal;

public record LoanRequest(
    BigDecimal loanAmount, BigDecimal interestRate, int installments, LoanType loanType) {}
