package com.bia.dev_bank.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Locale;

public enum PayedStatus {
  PAYED,
  TO_PAY,
  NOT_PAYED,
  PARTIAL;

  @JsonCreator
  public static PayedStatus fromString(String value) {
    if (value == null) {
      return null;
    }
    return PayedStatus.valueOf(value.trim().toUpperCase(Locale.ROOT));
  }

  @JsonValue
  public String toValue() {
    return this.name();
  }
}
