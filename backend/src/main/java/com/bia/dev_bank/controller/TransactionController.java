package com.bia.dev_bank.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bia.dev_bank.dto.transactionDTOs.TransactionRequest;
import com.bia.dev_bank.service.LoanPaymentsService;
import com.bia.dev_bank.service.TransactionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bia/transactions")
@Tag(name = "Transaction", description = "Endpoints for managing transactions and loan payments")
public class TransactionController {

    private final TransactionService transactionService;
    private final LoanPaymentsService loanPaymentsService;

    @Operation(summary = "createTransaction", description = "Performs a transaction from the origin account to the target account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction completed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid transaction data", content = @Content)
    })
    @PostMapping("/{originAccountNumber}")
    public ResponseEntity createTransaction(@RequestBody TransactionRequest request,
                                            @PathVariable String originAccountNumber){
        var transaction = transactionService.createTransaction(request,originAccountNumber);
        return ResponseEntity.ok().body(":) Parabéns sua transferência para "
                + transaction.ReceiverName() +" foi concretizada com sucesso");
    }
    
    @Operation(summary = "getTransactionByID", description = "Retrieves all transactions linked to a specific account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully")
    })
    @GetMapping("/{id}")
    public ResponseEntity getTransactionById(@PathVariable Long id){
        var transaction = transactionService.getTransactionById(id);
        return ResponseEntity.ok().body(transaction);
    }

    @Operation(summary = "getTransactionsByAccountNumber", description = "Retrieves all transactions in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of transactions retrieved successfully")
    })
    @GetMapping("/account/{accountNumber}")
    public ResponseEntity getTransactionsByAccountNumber(@PathVariable String accountNumber){
        var transactions = transactionService.getTransactionByAccountNumber(accountNumber);
        return ResponseEntity.ok().body(transactions);
    }

    @Operation(summary = "getAllTransactions", description = "Retrieves all transactions in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of transactions retrieved successfully")
    })
    @GetMapping("/list")
    public ResponseEntity getAllTransactions(){
        var transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok().body(transactions);
    }

    @Operation(summary = "transactionAddLoanPayments", description = "Adds a transaction as a loan payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loan payment added successfully"),
            @ApiResponse(responseCode = "404", description = "Loan payment not found", content = @Content)
    })
    @PostMapping("/loanPayments/{loanPaymentsId}")
    public ResponseEntity transactionAddLoanPayments(@PathVariable Long loanPaymentsId,@RequestBody TransactionRequest request){
        loanPaymentsService.addTransactionToLoanPayment(loanPaymentsId,request);
        return ResponseEntity.ok().body("Pago com sucesso");
    }

    @Operation(summary = "transactionsDelete", description = "Deletes a transaction by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Transaction not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity transactionsDelete(@PathVariable Long id){
        transactionService.transactionDelete(id);
        return ResponseEntity.ok().body("Transação foi excluida com sucesso a policia não vai te pegar :)");
    }
}
