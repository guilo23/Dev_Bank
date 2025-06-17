package com.bia.dev_bank.dto.payments;

import java.math.BigDecimal;

public record CardPaymentsRequest(
    String cardNumber, String productName, BigDecimal totalBuying, int installmentNumber) {}
