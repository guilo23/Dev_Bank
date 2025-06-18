package com.bia.dev_bank.dto.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record TransactionRequest(
    @NotNull
        @DecimalMin(value = "0.01", message = "amount must be greater than zero")
        @JsonProperty("amount")
        BigDecimal amount,
    @NotBlank(message = "destinyAccountNumber must not be blank")
        @JsonProperty("destinyAccountNumber")
        String destinyAccountNumber) {}
