package com.bia.dev_bank.controller;

import com.bia.dev_bank.dto.report.StatementResponse;
import com.bia.dev_bank.service.CardService;
import com.bia.dev_bank.service.ReportService;
import com.bia.dev_bank.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bia/report")
@Tag(name = "Report", description = "Endpoints for generating account and card transaction reports")
public class ReportController {

  private final CardService cardService;
  private final TransactionService transactionService;
  private final ReportService reportService;

  @Operation(
      summary = "transactionReport",
      description = "Retrieves the transaction report for a specific account number")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Transaction report generated successfully"),
    @ApiResponse(responseCode = "404", description = "Account not found", content = @Content)
  })
  @GetMapping("/transaction/{accountNumber}")
  public ResponseEntity<List<StatementResponse>> transactionReport(
      @PathVariable String accountNumber) {
    var report = transactionService.getStatementByAccountNumber(accountNumber);
    return ResponseEntity.status(HttpStatus.OK).body(report);
  }

  @Operation(
      summary = "cardDebitReport",
      description = "Retrieves all debit card transactions for a specific card number")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Debit card report generated successfully"),
    @ApiResponse(responseCode = "404", description = "Card not found", content = @Content)
  })
  @GetMapping("/debit/{cardNumber}")
  public ResponseEntity<List<StatementResponse>> cardDebitReport(@PathVariable String cardNumber) {
    var report = cardService.cardsDebitPaymentsReport(cardNumber);
    return ResponseEntity.status(HttpStatus.OK).body(report);
  }

  @Operation(
      summary = "cardCreditReport",
      description = "Retrieves all credit card transactions for a specific card number")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Credit card report generated successfully"),
    @ApiResponse(responseCode = "404", description = "Card not found", content = @Content)
  })
  @GetMapping("/credit/{cardNumber}")
  public ResponseEntity<List<StatementResponse>> cardCreditReport(@PathVariable String cardNumber) {
    var report = cardService.cardsCreditPaymentsReport(cardNumber);
    return ResponseEntity.status(HttpStatus.OK).body(report);
  }

  @Operation(
      summary = "allReports",
      description = "Retrieves all reports (transactions, credit, debit) for a specific account")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Full report generated successfully"),
    @ApiResponse(responseCode = "404", description = "Account not found", content = @Content)
  })
  @GetMapping("/all/{accountNumber}")
  public ResponseEntity<List<StatementResponse>> allReports(@PathVariable String accountNumber) {
    var reports = reportService.allcardReports(accountNumber);
    return ResponseEntity.status(HttpStatus.OK).body(reports);
  }
}
