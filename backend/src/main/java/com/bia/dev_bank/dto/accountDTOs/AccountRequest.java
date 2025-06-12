package com.bia.dev_bank.dto.accountDTOs;

import com.bia.dev_bank.entity.enums.AccountType;

public record AccountRequest(
                             AccountType accountType,
                             double currentBalance) {
}
