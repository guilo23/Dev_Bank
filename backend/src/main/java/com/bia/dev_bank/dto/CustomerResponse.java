package com.bia.dev_bank.dto;

public record CustomerResponse(String name,
                               String email,
                               String CPF,
                               String phoneNumber) {
}
