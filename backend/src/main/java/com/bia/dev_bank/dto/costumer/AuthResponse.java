package com.bia.dev_bank.dto.costumer;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthResponse(@JsonProperty("token") String token) {
  public String getToken() {
    return token;
  }
}
