package com.bia.dev_bank.dto.payments;

import com.bia.dev_bank.entity.CardPayments;
import java.math.BigDecimal;

public record CardPaymentsResponse(
    String cardNumber, String productName, int installmentNumber, BigDecimal installmentAmount) {
  public CardPaymentsResponse(CardPayments payments) {
    this(
        payments.getCard().getCardNumber(),
        payments.getProductName(),
        payments.getInstallmentNumber(),
        payments.getInstallmentAmount());
  }
}
