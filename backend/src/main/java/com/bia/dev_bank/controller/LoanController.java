package com.bia.dev_bank.controller;

import com.bia.dev_bank.dto.loanDTOs.LoanRequest;
import com.bia.dev_bank.dto.loanDTOs.LoanResponse;
import com.bia.dev_bank.service.LoanService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bia/loans")
@RequiredArgsConstructor
@Tag(name = "Loan", description = "Endpoints for managing loans")
public class LoanController {

  private final LoanService loanService;

  @Operation(summary = "createLoan", description = "Creates a new loan for a specific customer")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "Loan created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
      })
  @PostMapping("/{customerId}")
  public ResponseEntity<LoanResponse> createLoan(
      @PathVariable Long customerId, @RequestBody LoanRequest loan) {
    var createdLoan = loanService.createLoan(loan, customerId);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdLoan);
  }

  @Operation(summary = "getAllLoans", description = "Retrieves a list of all loans in the system")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "List of loans retrieved successfully")
      })
  @GetMapping
  public ResponseEntity<List<LoanResponse>> getAllLoans() {
    return ResponseEntity.ok(loanService.findAllLoans());
  }

  @Operation(summary = "getLoanByID", description = "Retrieves loan details by loan ID")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Loan found"),
        @ApiResponse(responseCode = "404", description = "Loan not found", content = @Content)
      })
  @GetMapping("/{id}")
  public ResponseEntity<LoanResponse> getLoanById(@PathVariable Long id) {
    var loan = loanService.findLoanById(id);
    return ResponseEntity.ok().body(loan);
  }
}
