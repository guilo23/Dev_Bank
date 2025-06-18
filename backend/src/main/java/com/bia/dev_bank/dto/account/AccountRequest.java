package com.bia.dev_bank.dto.account;

import com.bia.dev_bank.entity.enums.AccountType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record AccountRequest(
    @NotNull AccountType accountType,
    @NotNull(message = "You need put a number")
        @DecimalMin(value = "0.00", message = "The balance must be at least 0.00")
        BigDecimal currentBalance) {}
