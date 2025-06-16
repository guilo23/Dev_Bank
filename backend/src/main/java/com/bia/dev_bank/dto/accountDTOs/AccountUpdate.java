package com.bia.dev_bank.dto.accountDTOs;

import com.bia.dev_bank.entity.enums.AccountType;

import java.math.BigDecimal;

public record AccountUpdate(AccountType AccountType,
                            BigDecimal currentBalance
                            ) {
}
