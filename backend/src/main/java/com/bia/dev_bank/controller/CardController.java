package com.bia.dev_bank.controller;

import com.bia.dev_bank.dto.card.CreditRequest;
import com.bia.dev_bank.dto.card.CreditUpdate;
import com.bia.dev_bank.dto.payments.CardPaymentsRequest;
import com.bia.dev_bank.dto.payments.CardPaymentsResponse;
import com.bia.dev_bank.service.CardPaymentsService;
import com.bia.dev_bank.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bia/cards")
@Tag(name = "Card", description = "Endpoints for managing cards")
public class CardController {

  private final CardService cardService;
  private final CardPaymentsService cardPaymentsService;

  @Operation(
      summary = "Create a new card for an account",
      description = "Creates a new card associated with the specified account number.")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Card created successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid input data"),
    @ApiResponse(responseCode = "404", description = "Account not found")
  })
  @PostMapping("/add/{accountNumber}")
  public ResponseEntity createCard(
      @RequestBody @Valid CreditRequest request, @PathVariable String accountNumber) {
    var card = cardService.cardCreate(request, accountNumber);
    return ResponseEntity.status(HttpStatus.CREATED).body(card);
  }

  @Operation(
      summary = "Add a credit card purchase",
      description = "Registers a new purchase on the credit card and updates the card balance.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Credit card purchase added successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid purchase details"),
    @ApiResponse(responseCode = "404", description = "Card not found")
  })
  @PostMapping("/credit")
  public ResponseEntity addCreditBuying(@RequestBody @Valid CardPaymentsRequest request) {
    var card = cardService.addCreditCardPayment(request);
    return ResponseEntity.status(HttpStatus.OK).body(card);
  }

  @Operation(
      summary = "Add a debit card purchase",
      description =
          "Registers a new purchase using the debit card, deducting the amount from the linked account.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Debit card purchase added successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid purchase details"),
    @ApiResponse(responseCode = "404", description = "Card or account not found")
  })
  @PostMapping("/debit")
  public ResponseEntity addDebitBuying(@RequestBody @Valid CardPaymentsRequest request) {
    var card = cardService.addDebitCardPayment(request);
    cardPaymentsService.addTransactionToCardPayments(card.getId());
    return ResponseEntity.status(HttpStatus.OK).body(new CardPaymentsResponse(card));
  }

  @Operation(summary = "getCardByID", description = "Retrieves card details by card ID")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Card found"),
    @ApiResponse(responseCode = "404", description = "Card not found", content = @Content)
  })
  @GetMapping("/{cardId}")
  public ResponseEntity getCardById(@PathVariable Long cardId) {
    var card = cardService.getCardById(cardId);
    return ResponseEntity.status(HttpStatus.OK).body(card);
  }

  @Operation(
      summary = "getListAllCardByAccount",
      description = "Retrieves all cards linked to a specific account")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "List of cards retrieved successfully")
  })
  @GetMapping("/list/{accountNumber}")
  public ResponseEntity getAllCardByAccount(@PathVariable String accountNumber) {
    var cards = cardService.getAllCardByAccountNumber(accountNumber);
    return ResponseEntity.status(HttpStatus.OK).body(cards);
  }

  @Operation(summary = "cardUpdate", description = "Updates card details by card ID")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Card updated successfully"),
    @ApiResponse(responseCode = "404", description = "Card not found", content = @Content)
  })
  @PutMapping("/{id}")
  public ResponseEntity cardUpdate(@PathVariable Long id, @RequestBody @Valid CreditUpdate update) {
    var card = cardService.cardUpdate(update, id);
    return ResponseEntity.status(HttpStatus.OK).body(card);
  }

  @Operation(summary = "cardDelete", description = "Deletes a card by its ID")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Card deleted successfully"),
    @ApiResponse(responseCode = "404", description = "Card not found", content = @Content)
  })
  @DeleteMapping("/{id}")
  public ResponseEntity cardDelete(@PathVariable Long id) {
    cardService.cardDelete(id);
    return ResponseEntity.status(HttpStatus.OK).body("cartão deletado com sucesso");
  }
}
