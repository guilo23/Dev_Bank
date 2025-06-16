package com.bia.dev_bank.dto.transactionDTOs;

import java.math.BigDecimal;

public record TransactionRequest(BigDecimal amount,
                                 String destinyAccountnumber,
                                 Long loanPaymentsId) {
}
