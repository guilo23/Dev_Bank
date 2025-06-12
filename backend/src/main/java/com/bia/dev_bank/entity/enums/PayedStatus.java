package com.bia.dev_bank.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PayedStatus {
    PAYED, TO_PAY, NOT_PAYED, PARTIAL;

    @JsonCreator
    public static LoanType fromString(String value) {
        if (value == null) return null;
        return LoanType.valueOf(value.trim().toUpperCase());
    }
    @JsonValue
    public String toValue() {
        return this.name();
    }
}
