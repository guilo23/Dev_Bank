package com.bia.dev_bank.dto.reportDTOs;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record StatementResponse(String type,
                                BigDecimal amount,
                                LocalDate timeStamp,
                                String description
                                ) {
}
