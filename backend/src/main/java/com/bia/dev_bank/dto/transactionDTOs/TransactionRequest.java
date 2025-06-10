package com.bia.dev_bank.dto.transactionDTOs;

public record TransactionRequest(Double amount,
                                 String destinyAccountnumber) {
}
