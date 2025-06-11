package com.bia.dev_bank.dto.AccountDTOs;

import com.bia.dev_bank.entity.enums.AccountType;

public record AccountRequest(
                             AccountType accountType,
                             double currentBalance) {
}
