package com.bia.dev_bank.dto.AccountDTOs;

public record AccountRequest(
                             String AccountType,
                             double currentBalance) {
}
