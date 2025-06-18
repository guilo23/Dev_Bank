package com.bia.dev_bank.dto.costumer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CustomerRequest(
    @NotBlank(message = "Name is required") String name,
    @NotBlank(message = "Email is required") @Email(message = "Email must be valid") String email,
    @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        String password,
    @NotBlank(message = "Birthday is required") String birthday,
    @NotBlank(message = "CPF is required")
        @Pattern(
            regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}",
            message = "CPF must be in format XXX.XXX.XXX-XX")
        String CPF,
    @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "\\d{10,11}", message = "Phone number must have 10 or 11 digits")
        String phoneNumber) {}
