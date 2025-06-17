package com.bia.dev_bank.dto.transaction;

import com.bia.dev_bank.entity.Transaction;
import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionResponse(
    BigDecimal amount, String ReceiverName, String SenderName, LocalDate transactionDate) {
  public TransactionResponse(Transaction transaction) {
    this(
        transaction.getAmount(),
        transaction.getDestinyAccount().getCustomer().getName() != null
            ? transaction.getDestinyAccount().getCustomer().getName()
            : "",
        transaction.getOriginAccount().getCustomer().getName() != null
            ? transaction.getOriginAccount().getCustomer().getName()
            : "",
        transaction.getTransactionDate());
  }
}
