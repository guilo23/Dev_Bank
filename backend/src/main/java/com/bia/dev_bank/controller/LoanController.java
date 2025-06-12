package com.bia.dev_bank.controller;

import com.bia.dev_bank.dto.loanDTOs.LoanRequest;
import com.bia.dev_bank.dto.loanDTOs.LoanResponse;
import com.bia.dev_bank.entity.Loan;
import com.bia.dev_bank.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bia/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping("/{customerId}")
    public ResponseEntity<LoanResponse> createLoan(
            @PathVariable Long customerId,
            @RequestBody LoanRequest loan
    ) {
        var createdLoan = loanService.createLoan(loan, customerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLoan);
    }

    @GetMapping
    public ResponseEntity<List<LoanResponse>> getAllLoans() {
        return ResponseEntity.ok(loanService.findAllLoans());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanResponse> getLoanById(@PathVariable Long id) {
        var loan = loanService.findLoanById(id);
        return ResponseEntity.ok().body(loan);
    }
}