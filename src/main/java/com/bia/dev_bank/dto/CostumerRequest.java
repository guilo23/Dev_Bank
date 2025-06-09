package com.bia.dev_bank.dto;

import jakarta.persistence.Column;

public record CostumerRequest(String name,
                              String email,
                              String password,
                              String birthday,
                              String CPF,
                              String phoneNumber) {
}
