package com.bia.dev_bank.controller;

import com.bia.dev_bank.dto.reportDTOs.StatementResponse;
import com.bia.dev_bank.entity.enums.AccountType;
import com.bia.dev_bank.entity.enums.CardType;
import com.bia.dev_bank.service.CardService;
import com.bia.dev_bank.service.ReportService;
import com.bia.dev_bank.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bia/report")
public class ReportController {
    private final CardService cardService;
    private final TransactionService transactionService;
    private final ReportService reportService;

    @GetMapping("/transaction/{accountNumber}")
    public ResponseEntity<List<StatementResponse>> transactionReport(@PathVariable String accountNumber){
        var report = transactionService.getStatementByAccountNumber(accountNumber);
        return ResponseEntity.status(HttpStatus.OK).body(report);
    }
    @GetMapping("/debit/{cardNumber}")
    public ResponseEntity<List<StatementResponse>> CardDebitReport(@PathVariable String cardNumber){
        var report = cardService.cardsDebitPaymentsReport(cardNumber);
        return ResponseEntity.status(HttpStatus.OK).body(report);
    }
    @GetMapping("/credit/{cardNumber}")
    public ResponseEntity<List<StatementResponse>> CardCreditReport(@PathVariable String cardNumber){
        var report = cardService.cardsCreditPaymentsReport(cardNumber);
        return ResponseEntity.status(HttpStatus.OK).body(report);
    }
    @GetMapping("/all/{accountNumber}")
    public ResponseEntity<List<StatementResponse>> allReports(@PathVariable String accountNumber){
        var reports = reportService.allcardReports(accountNumber);
       return ResponseEntity.status(HttpStatus.OK).body(reports);
    }

}
