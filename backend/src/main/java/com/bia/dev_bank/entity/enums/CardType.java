package com.bia.dev_bank.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CardType {
    DEBIT,
    CREDIT;

    @JsonCreator
    public static CardType fromString(String value) {
        if (value == null) return null;
        return CardType.valueOf(value.trim().toUpperCase());
    }
    @JsonValue
    public String toValue() {
        return this.name();
    }
}

