package com.bia.dev_bank.controller;

import com.bia.dev_bank.config.*;
import com.bia.dev_bank.dto.payments.*;
import com.bia.dev_bank.repository.*;
import com.bia.dev_bank.service.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.security.*;
import io.swagger.v3.oas.annotations.tags.*;
import jakarta.persistence.*;
import java.math.*;
import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bia/payments")
@Tag(name = "Payment", description = "Endpoints for managing payments")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class PaymentController {

  private final LoanPaymentsService loanPaymentsService;
  private final CardPaymentsService cardPaymentsService;
  private final CardPaymentsRepository cardPaymentsRepository;

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

  @GetMapping("/card-payments")
  public ResponseEntity getCardPaymentsInstallments(@RequestParam Long cardId) {
    var payments = cardPaymentsService.getCardPaymentsreportByid(cardId);
    return ResponseEntity.status(HttpStatus.OK).body(payments);
  }

  @PostMapping("/billing")
  public ResponseEntity payInstallment(@RequestBody PayRequest request) {
    var payment =
        cardPaymentsRepository
            .findByProductNameAndInstallmentNumber(request.productName(), request.installment())
            .orElseThrow(
                () -> new EntityNotFoundException("installment not found or already paid"));
    cardPaymentsService.addTransactionToCardPayments(
        payment.getId(), payment.getInstallmentAmount());
    return ResponseEntity.status(HttpStatusCode.valueOf(200)).build();
  }

  @GetMapping("/billing/{cardId}")
  public ResponseEntity getActualBilling(@PathVariable Long cardId, @RequestParam String month) {
    var cards = cardPaymentsService.getActualBilling(cardId, month);
    return ResponseEntity.status(HttpStatus.OK).body(cards);
  }

  @PostMapping("/creditPayment/{cardPaymentId}")
  public ResponseEntity addCreditPayment(
      @PathVariable Long cardPaymentId, @RequestBody Double payedValue) {
    cardPaymentsService.addTransactionToCardPayments(cardPaymentId, BigDecimal.valueOf(payedValue));
    return ResponseEntity.status(HttpStatus.OK).build();
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
