package com.bia.dev_bank.controller;

import com.bia.dev_bank.dto.CardPaymentsDTOs.CardPaymentsRequest;
import com.bia.dev_bank.repository.CardPaymentsRepository;
import com.bia.dev_bank.service.CardPaymentsService;
import com.bia.dev_bank.service.CardService;
import com.bia.dev_bank.service.LoanPaymentsService;
import com.bia.dev_bank.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bia/payments")
public class PaymentController {

    private final LoanService loanService;
    private final CardService cardService;
    private final LoanPaymentsService loanPaymentsService;
    private final CardPaymentsService cardPaymentsService;

    @PostMapping("/loan")
    public ResponseEntity getLoanPaymentById(Long loanPaymentId){
    var loan = loanPaymentsService.getLoanPaymentsById(loanPaymentId);
    return ResponseEntity.status(HttpStatus.OK).body(loan);
}
    @PostMapping("/card")
    public ResponseEntity getCardPaymentById(Long cardPaymentId){
        var card = cardPaymentsService.getCardPaymentsById(cardPaymentId);

        return ResponseEntity.status(HttpStatus.OK).body(card);
    }
}
