package com.bia.dev_bank.dto.loan;

import com.bia.dev_bank.entity.enums.LoanType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record LoanRequest(
    @NotNull BigDecimal loanAmount,
    @NotNull @DecimalMin(value = "0.00", message = "the interestRate need to be greater than zero")
        BigDecimal interestRate,
    @NotNull @Min(value = 1, message = "a minimum of intallments is at least 1") int installments,
    @NotNull(message = "need to choose a valid type of loan") LoanType loanType) {}
