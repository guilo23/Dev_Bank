package com.bia.dev_bank.dto.cardDTOs;

import com.bia.dev_bank.entity.Card;
import java.math.BigDecimal;

public record CardResponse(String cardNumber, BigDecimal cardLimit, String CustomerName) {
  public CardResponse(Card card) {
    this(card.getCardNumber(), card.getCardLimit(), card.getAccount().getCustomer().getName());
  }
}
