package com.bia.dev_bank.dto.costumer;

public record CustomerRequest(
    String name, String email, String password, String birthday, String CPF, String phoneNumber) {}
