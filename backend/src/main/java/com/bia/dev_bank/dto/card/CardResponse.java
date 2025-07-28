package com.bia.dev_bank.dto.card;

import com.bia.dev_bank.entity.Card;
import com.bia.dev_bank.entity.enums.*;
import java.math.BigDecimal;

public record CardResponse(
    Long cardId,
    CardType cardType,
    String cardNumber,
    BigDecimal cardLimit,
    BigDecimal cardBilling,
    String customerName) {
  public CardResponse(Card card) {
    this(
        card.getId(),
        card.getCardType(),
        card.getCardNumber(),
        card.getCardLimit(),
        card.getCardBilling(),
        card.getAccount().getCustomer().getName());
  }
}
