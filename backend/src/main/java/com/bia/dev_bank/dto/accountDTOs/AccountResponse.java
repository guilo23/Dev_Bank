package com.bia.dev_bank.dto.accountDTOs;

import com.bia.dev_bank.entity.Account;
import com.bia.dev_bank.entity.enums.AccountType;
import java.math.BigDecimal;

public record AccountResponse(
    String accountNumber, String customerName, AccountType accountType, BigDecimal currentBalance) {
  public AccountResponse(Account account) {
    this(
        account.getAccountNumber(),
        account.getCustomer().getName(),
        account.getAccountType(),
        account.getCurrentBalance());
  }
}
