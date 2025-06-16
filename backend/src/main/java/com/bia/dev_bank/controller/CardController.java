package com.bia.dev_bank.controller;

import com.bia.dev_bank.dto.cardDTOs.CardRequest;
import com.bia.dev_bank.dto.cardDTOs.CardUpdate;
import com.bia.dev_bank.service.CardService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bia/cards")
@Tag(name = "Card", description = "Endpoints for managing cards")
public class CardController {

    private final CardService cardService;

    @Operation(summary = "CreateCard", description = "Creates a new card linked to a specific account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Card created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping("/{accountNumber}")
    public ResponseEntity createCard(@RequestBody CardRequest request,
            @PathVariable String accountNumber) {
        var card = cardService.cardCreate(request, accountNumber);
        return ResponseEntity.status(HttpStatus.CREATED).body(card);
    }

    @Operation(summary = "getCardByID", description = "Retrieves card details by card ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card found"),
            @ApiResponse(responseCode = "404", description = "Card not found", content = @Content)
    })
    @GetMapping("/{cardId}")
    public ResponseEntity getCardById(@PathVariable Long cardId) {
        var card = cardService.getCardById(cardId);
        return ResponseEntity.status(HttpStatus.OK).body(card);
    }

    @Operation(summary = "getListAllCardByAccount", description = "Retrieves all cards linked to a specific account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of cards retrieved successfully")
    })
    @GetMapping("/list/{accountNumber}")
    public ResponseEntity getAllCardByAccount(@PathVariable String accountNumber) {
        var cards = cardService.getAllCardByAccountNumber(accountNumber);
        return ResponseEntity.status(HttpStatus.OK).body(cards);
    }

    @Operation(summary = "cardUpdate", description = "Updates card details by card ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card updated successfully"),
            @ApiResponse(responseCode = "404", description = "Card not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity CardUpdate(@PathVariable Long id,
            @RequestBody CardUpdate update) {
        var card = cardService.cardUpdate(update, id);
        return ResponseEntity.status(HttpStatus.OK).body(card);
    }

    @Operation(summary = "cardDelete", description = "Deletes a card by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Card not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity CardDelete(@PathVariable Long id) {
        cardService.cardDelete(id);
        return ResponseEntity.status(HttpStatus.OK).body("cartão deletado com sucesso");
    }
}
