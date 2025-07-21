package com.bia.dev_bank.controller;

import com.bia.dev_bank.dto.loan.LoanRequest;
import com.bia.dev_bank.dto.loan.LoanResponse;
import com.bia.dev_bank.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bia/loans")
@RequiredArgsConstructor
@Tag(name = "Loan", description = "Endpoints for managing loans")
public class LoanController {

  private static final Logger logger = LoggerFactory.getLogger(LoanController.class);

  private final LoanService loanService;

  @Operation(summary = "createLoan", description = "Creates a new loan for a specific customer")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Loan created successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
  })
  @PreAuthorize("#account.customer.id == principal.id")
  @PostMapping("/{customerId}")
  public ResponseEntity<LoanResponse> createLoan(
      @PathVariable Long customerId, @RequestBody @Valid LoanRequest loan) {
    logger.info("Request received to create loan for customer {}", customerId);
    var createdLoan = loanService.createLoan(loan, customerId);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdLoan);
  }

  @Operation(summary = "getAllLoans", description = "Retrieves a list of all loans in the system")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "List of loans retrieved successfully")
  })
  @GetMapping
  public ResponseEntity<List<LoanResponse>> getAllLoans() {
    logger.info("Request received to get all loans");
    return ResponseEntity.ok(loanService.findAllLoans());
  }

  @Operation(summary = "getLoanByID", description = "Retrieves loan details by loan ID")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Loan found"),
    @ApiResponse(responseCode = "404", description = "Loan not found", content = @Content)
  })
  @PreAuthorize("#account.customer.id == principal.id")
  @GetMapping("/{id}")
  public ResponseEntity<LoanResponse> getLoanById(@PathVariable Long id) {
    logger.info("Request received to get loan {}", id);
    var loan = loanService.findLoanById(id);
    return ResponseEntity.ok().body(loan);
  }
}
