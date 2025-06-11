package com.bia.dev_bank.dto.AccountDTOs;

import com.bia.dev_bank.entity.enums.AccountType;

public record AccountUpdate(AccountType AccountType,
                            double currentBalance
                            ) {
}
