package com.bia.dev_bank.dto.card;

import com.bia.dev_bank.entity.enums.CardType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DebitRequest(
    @NotNull(message = "you choose a valid account type") CardType cardType,
    @NotBlank(message = "your card need have a number") String cardNumber) {}
