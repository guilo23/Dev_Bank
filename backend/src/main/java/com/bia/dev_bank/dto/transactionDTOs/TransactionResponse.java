package com.bia.dev_bank.dto.transactionDTOs;

import com.bia.dev_bank.entity.Account;
import com.bia.dev_bank.entity.Transaction;

import java.time.LocalDate;

public record TransactionResponse(Double amount,
                                  String name,
                                  LocalDate transactionDate) {
    public TransactionResponse(Transaction transaction) {
        this(
                transaction.getAmount(),
                transaction.getDestinyAccount().getCustomer().getName(),
                transaction.getTransactionDate()
        );
    }

}
