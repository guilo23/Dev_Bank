package com.bia.dev_bank.dto.card;

import com.bia.dev_bank.entity.enums.CardType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CreditRequest(
    @NotNull(message = "you choose a valid account type") CardType cardType,
    @NotBlank(message = "your card need have a number") String cardNumber,
    @NotNull() @DecimalMin(value = "0.00", message = "The balance must be at least 0.00")
        BigDecimal cardLimit) {}
