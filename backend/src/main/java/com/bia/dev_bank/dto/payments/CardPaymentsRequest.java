package com.bia.dev_bank.dto.payments;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CardPaymentsRequest(
    @NotBlank(message = "your card need have a number") String cardNumber,
    @NotBlank(message = "you need buy something") String productName,
    BigDecimal totalBuying,
    @NotNull @Min(value = 1, message = "a minimum of intallments is at least 1")
        int installmentNumber) {}
