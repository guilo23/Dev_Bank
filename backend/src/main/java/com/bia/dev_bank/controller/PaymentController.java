package com.bia.dev_bank.controller;

import com.bia.dev_bank.config.SecurityConfig;
import com.bia.dev_bank.service.CardPaymentsService;
import com.bia.dev_bank.service.LoanPaymentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bia/payments")
@Tag(name = "Payment", description = "Endpoints for managing payments")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class PaymentController {

  private final LoanPaymentsService loanPaymentsService;
  private final CardPaymentsService cardPaymentsService;

  @Operation(
      summary = "getLoanPaymentByID",
      description = "Retrieves loan payment details for the specified loan payment ID.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Loan payment details retrieved successfully"),
    @ApiResponse(responseCode = "404", description = "Loan payment not found"),
    @ApiResponse(
        responseCode = "401",
        description = "Unauthorized - Invalid or missing authentication token"),
    @ApiResponse(
        responseCode = "403",
        description = "Forbidden - The user does not have permission to access this resource")
  })
  @GetMapping("/loan")
  public ResponseEntity getLoanPaymentById(Long loanPaymentId) {
    var loan = loanPaymentsService.getLoanPaymentsById(loanPaymentId);
    return ResponseEntity.status(HttpStatus.OK).body(loan);
  }

  @Operation(
      summary = "getCardPaymentByID",
      description = "Retrieves card payment details for the specified card payment ID.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Card payment details retrieved successfully"),
    @ApiResponse(responseCode = "404", description = "Card payment not found"),
    @ApiResponse(
        responseCode = "401",
        description = "Unauthorized - Invalid or missing authentication token"),
    @ApiResponse(
        responseCode = "403",
        description = "Forbidden - The user does not have permission to access this resource")
  })
  @GetMapping("/card/{cardPaymentId}")
  public ResponseEntity getCardPaymentById(@PathVariable Long cardPaymentId) {
    var card = cardPaymentsService.getCardPaymentsById(cardPaymentId);
    return ResponseEntity.status(HttpStatus.OK).body(card);
  }
}
