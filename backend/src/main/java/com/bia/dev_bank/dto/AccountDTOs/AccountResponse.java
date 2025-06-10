package com.bia.dev_bank.dto.AccountDTOs;

import com.bia.dev_bank.entity.Account;

public record AccountResponse(
        String accountNumber,
        String customerName,
        String accountType,
        double currentBalance
) {
    public AccountResponse(Account account) {
        this(
                account.getAccountNumber(),
                account.getAccountType(),
                account.getCustomer().getName(),
                account.getCurrentBalance()
        );
    }
}
