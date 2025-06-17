package com.bia.dev_bank.dto.card;

import com.bia.dev_bank.entity.enums.CardType;
import java.math.BigDecimal;

public record CardRequest(CardType cardType, String cardNumber, BigDecimal cardLimit) {}
