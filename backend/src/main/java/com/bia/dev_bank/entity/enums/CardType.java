package com.bia.dev_bank.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Locale;

public enum CardType {
  DEBIT,
  CREDIT;

  @JsonCreator
  public static CardType fromString(String value) {
    if (value == null) {
      return null;
    }
    return CardType.valueOf(value.trim().toUpperCase(Locale.ROOT));
  }

  @JsonValue
  public String toValue() {
    return this.name();
  }
}
