package com.bia.dev_bank.dto.card;

import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;

public record CreditUpdate(
    @DecimalMin(value = "500.00", message = "The card limit must be at least 500.00")
        BigDecimal cardLimit) {}
