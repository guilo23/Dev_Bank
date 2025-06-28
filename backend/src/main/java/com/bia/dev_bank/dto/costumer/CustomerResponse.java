package com.bia.dev_bank.dto.costumer;

public record CustomerResponse(
    Long id, String name, String email, String CPF, String phoneNumber) {}
