package com.bia.dev_bank.dto.transaction;

import java.math.BigDecimal;

public record TransactionRequest(
    BigDecimal amount, String destinyAccountnumber, Long loanPaymentsId) {}
