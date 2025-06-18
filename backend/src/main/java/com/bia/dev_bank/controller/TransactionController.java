package com.bia.dev_bank.controller;

import com.bia.dev_bank.dto.transaction.TransactionRequest;
import com.bia.dev_bank.service.LoanPaymentsService;
import com.bia.dev_bank.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bia/transactions")
@Validated
@Tag(name = "Transaction", description = "Endpoints for managing transactions and loan payments")
public class TransactionController {
  private final ObjectMapper objectMapper;
  private final TransactionService transactionService;
  private final LoanPaymentsService loanPaymentsService;

  @Operation(
      summary = "createTransaction",
      description = "Performs a transaction from the origin account to the target account")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Transaction completed successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid transaction data", content = @Content)
  })
  @PostMapping("/{originAccountNumber}")
  public ResponseEntity createTransaction(
      @RequestBody @Valid TransactionRequest request, @PathVariable String originAccountNumber) {
    var transaction = transactionService.createTransaction(request, originAccountNumber);
    System.out.println(request);
    return ResponseEntity.ok()
        .body(
            ":) Parabéns sua transferência para "
                + transaction.receiverName()
                + " foi concretizada com sucesso");
  }

  @Operation(
      summary = "getTransactionByID",
      description = "Retrieves all transactions linked to a specific account")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully")
  })
  @GetMapping("/{id}")
  public ResponseEntity getTransactionById(@PathVariable Long id) {
    var transaction = transactionService.getTransactionById(id);
    return ResponseEntity.ok().body(transaction);
  }

  @Operation(
      summary = "getTransactionsByAccountNumber",
      description = "Retrieves all transactions in the system")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "List of transactions retrieved successfully")
  })
  @GetMapping("/account/{accountNumber}")
  public ResponseEntity getTransactionsByAccountNumber(@PathVariable String accountNumber) {
    var transactions = transactionService.getTransactionByAccountNumber(accountNumber);
    return ResponseEntity.ok().body(transactions);
  }

  @Operation(
      summary = "getAllTransactions",
      description = "Retrieves all transactions in the system")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "List of transactions retrieved successfully")
  })
  @GetMapping("/list")
  public ResponseEntity getAllTransactions() {
    var transactions = transactionService.getAllTransactions();
    return ResponseEntity.ok().body(transactions);
  }

  @Operation(
      summary = "transactionAddLoanPayments",
      description = "Adds a transaction as a loan payment")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Loan payment added successfully"),
    @ApiResponse(responseCode = "404", description = "Loan payment not found", content = @Content)
  })
  @PostMapping("/loanPayments/{loanPaymentsId}")
  public ResponseEntity transactionAddLoanPayments(
      @PathVariable Long loanPaymentsId, @RequestBody @Valid TransactionRequest request) {
    loanPaymentsService.addTransactionToLoanPayment(loanPaymentsId, request);
    return ResponseEntity.ok().body("Pago com sucesso");
  }

  @Operation(summary = "transactionsDelete", description = "Deletes a transaction by its ID")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Transaction deleted successfully"),
    @ApiResponse(responseCode = "404", description = "Transaction not found", content = @Content)
  })
  @DeleteMapping("/{id}")
  public ResponseEntity transactionsDelete(@PathVariable Long id) {
    transactionService.transactionDelete(id);
    return ResponseEntity.ok()
        .body("Transação foi excluida com sucesso a policia não vai te pegar :)");
  }
}
