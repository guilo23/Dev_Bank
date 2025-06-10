package com.bia.dev_bank.dto;

public record AccountRequest(String AccountNumber,
                             String AccountType,
                             double currentBalance) {
}
