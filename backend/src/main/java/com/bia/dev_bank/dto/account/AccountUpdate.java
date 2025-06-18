package com.bia.dev_bank.dto.account;

import com.bia.dev_bank.entity.enums.AccountType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record AccountUpdate(
    AccountType AccountType,
    @NotNull @DecimalMin(value = "0.00", message = "The balance must be at least 0.00")
        BigDecimal currentBalance) {}
