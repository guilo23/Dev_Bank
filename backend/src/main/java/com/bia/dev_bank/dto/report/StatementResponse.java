package com.bia.dev_bank.dto.report;

import java.math.BigDecimal;
import java.time.LocalDate;

public record StatementResponse(
    String type, BigDecimal amount, LocalDate timeStamp, String description) {}
