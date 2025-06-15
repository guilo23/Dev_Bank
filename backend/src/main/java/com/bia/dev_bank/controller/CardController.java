package com.bia.dev_bank.controller;

import com.bia.dev_bank.dto.CardPaymentsDTOs.CardPaymentsRequest;
import com.bia.dev_bank.dto.CardPaymentsDTOs.CardPaymentsResponse;
import com.bia.dev_bank.dto.cardDTOs.CardRequest;
import com.bia.dev_bank.dto.cardDTOs.CardUpdate;
import com.bia.dev_bank.service.CardPaymentsService;
import com.bia.dev_bank.service.CardService;
import com.bia.dev_bank.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bia/cards")
public class CardController {

    private final CardService cardService;
    private final CardPaymentsService cardPaymentsService;

    @PostMapping("/add/{accountNumber}")
    public ResponseEntity createCard(@RequestBody CardRequest request,
                                     @PathVariable String accountNumber) {
        var card = cardService.cardCreate(request, accountNumber);

        return ResponseEntity.status(HttpStatus.CREATED).body(card);
    }
    @PostMapping("/credit")
    public ResponseEntity addCreditBuying(@RequestBody CardPaymentsRequest request){
        var card = cardService.addCreditCardPayment(request);
        return ResponseEntity.status(HttpStatus.OK).body(card);
    }
    @PostMapping("/debit")
    public ResponseEntity addDebitBuying(@RequestBody CardPaymentsRequest request){
        var card = cardService.addDebitCardPayment(request);
        cardPaymentsService.addTransactionToCardPayments(card.getId());
        return ResponseEntity.status(HttpStatus.OK).body(new CardPaymentsResponse(card));
    }
    @GetMapping("/{cardId}")
    public ResponseEntity getCardById(@PathVariable Long cardId){
        var card = cardService.getCardById(cardId);

        return ResponseEntity.status(HttpStatus.OK).body(card);
    }
    @GetMapping("/list/{accountNumber}")
    public ResponseEntity getAllCardByAccount(@PathVariable String accountNumber){
        var cards = cardService.getAllCardByAccountNumber(accountNumber);

        return ResponseEntity.status(HttpStatus.OK).body(cards);
    }
    @PutMapping("/{id}")
    public ResponseEntity CardUpdate(@PathVariable Long id,
                                     @RequestBody CardUpdate update){
        var card = cardService.cardUpdate(update,id);

        return ResponseEntity.status(HttpStatus.OK).body(card);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity CardDelete(@PathVariable Long id){
        cardService.cardDelete(id);

        return ResponseEntity.status(HttpStatus.OK).body("cartão deletado com sucesso");
    }
}
