package com.bia.dev_bank.dto.card;

import com.bia.dev_bank.entity.Card;
import java.math.BigDecimal;

public record CardResponse(
    Long CardId, String cardNumber, BigDecimal cardLimit, String CustomerName) {
  public CardResponse(Card card) {
    this(
        card.getId(),
        card.getCardNumber(),
        card.getCardLimit(),
        card.getAccount().getCustomer().getName());
  }
}
