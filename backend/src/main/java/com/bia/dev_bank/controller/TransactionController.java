package com.bia.dev_bank.controller;

import com.bia.dev_bank.dto.transactionDTOs.TransactionRequest;
import com.bia.dev_bank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/bia/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/{originAccountNumber}")
    public ResponseEntity createTransaction(@RequestBody TransactionRequest request,
                                            @PathVariable String originAccountNumber){
        var transaction = transactionService.createTransaction(request,originAccountNumber);
        return ResponseEntity.ok().body(":) Parabéns sua transferência para "
                + transaction.name() +" foi concretizada com sucesso");
    }
    @GetMapping("/{id}")
    public ResponseEntity getTransactionById(@PathVariable Long id){
        var transaction = transactionService.getTransactionById(id);
        return ResponseEntity.ok().body(transaction);
    }
    @GetMapping("/account/{accountNumber}")
    public ResponseEntity getTransactionsByAccountNumber(@PathVariable String accountNumber){
        var transactions = transactionService.getTransactionByAccountNumber(accountNumber);
        System.out.println(transactions);
        return ResponseEntity.ok().body(transactions);
    }
    @GetMapping("/list")
    public ResponseEntity getAllTransactions(){
        var transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok().body(transactions);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity transactionsDelete(@PathVariable Long id){
        transactionService.transactionDelete(id);
        return ResponseEntity.ok().body("Transação foi excluida com sucesso a policia não vai te pegar :)");
    }
}
