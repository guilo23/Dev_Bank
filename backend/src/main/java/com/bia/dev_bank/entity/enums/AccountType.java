package com.bia.dev_bank.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AccountType {
  CHECKING,
  SAVINGS;

  @JsonCreator
  public static AccountType fromString(String value) {
    if (value == null) return null;
    return AccountType.valueOf(value.trim().toUpperCase());
  }

  @JsonValue
  public String toValue() {
    return this.name();
  }
}
