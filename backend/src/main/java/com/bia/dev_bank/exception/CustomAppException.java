package com.bia.dev_bank.exception;

import org.springframework.http.HttpStatus;

public class CustomAppException extends Exception {
  private final HttpStatus status;

  public CustomAppException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }

  public HttpStatus getStatus() {
    return status;
  }
}
