package com.bia.dev_bank.controller;

import com.bia.dev_bank.dto.cardDTOs.CardRequest;
import com.bia.dev_bank.dto.cardDTOs.CardUpdate;
import com.bia.dev_bank.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bia/cards")
public class CardController {

    private final CardService cardService;

    @PostMapping("/{accountNumber}")
    public ResponseEntity createCard(@RequestBody CardRequest request,
                                     @PathVariable String accountNumber){
      var card = cardService.cardCreate(request,accountNumber);

      return ResponseEntity.status(HttpStatus.CREATED).body(card);
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
