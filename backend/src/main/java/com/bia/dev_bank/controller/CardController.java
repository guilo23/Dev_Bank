package com.bia.dev_bank.controller;

import com.bia.dev_bank.config.*;
import com.bia.dev_bank.dto.card.*;
import com.bia.dev_bank.dto.payments.*;
import com.bia.dev_bank.repository.*;
import com.bia.dev_bank.service.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.security.*;
import io.swagger.v3.oas.annotations.tags.*;
import jakarta.persistence.*;
import jakarta.validation.*;
import java.math.*;
import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bia/cards")
@Tag(name = "Card", description = "Endpoints for managing cards")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class CardController {

  private final CardService cardService;
  private final CardPaymentsService cardPaymentsService;
  private final CardPaymentsRepository cardPaymentsRepository;

  @Operation(
      summary = "Create a new card for an account",
      description = "Creates a new card associated with the specified account number.")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Card created successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid input data"),
    @ApiResponse(responseCode = "404", description = "Account not found"),
    @ApiResponse(
        responseCode = "401",
        description = "Unauthorized - Invalid or missing authentication token"),
    @ApiResponse(
        responseCode = "403",
        description = "Forbidden - The user does not have permission to access this resource")
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
    @ApiResponse(responseCode = "404", description = "Card not found"),
    @ApiResponse(
        responseCode = "401",
        description = "Unauthorized - Invalid or missing authentication token"),
    @ApiResponse(
        responseCode = "403",
        description = "Forbidden - The user does not have permission to access this resource")
  })
  @PostMapping("/credit")
  public ResponseEntity addCreditBuying(@RequestBody @Valid CardPaymentsRequest request) {
    var card = cardService.addCreditCardPayment(request);
    return ResponseEntity.status(HttpStatus.OK).body(card);
  }

  @PatchMapping("{cardId}/limit")
  public ResponseEntity addLimit(@RequestParam BigDecimal plusLimit, @PathVariable Long cardId) {
    cardService.increaseLimit(plusLimit, cardId);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @Operation(
      summary = "Add a debit card purchase",
      description =
          "Registers a new purchase using the debit card, deducting the amount from the linked account.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Debit card purchase added successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid purchase details"),
    @ApiResponse(responseCode = "404", description = "Card or account not found"),
    @ApiResponse(
        responseCode = "401",
        description = "Unauthorized - Invalid or missing authentication token"),
    @ApiResponse(
        responseCode = "403",
        description = "Forbidden - The user does not have permission to access this resource")
  })
  @PostMapping("/debit")
  public ResponseEntity addDebitBuying(@RequestBody @Valid CardPaymentsRequest request) {
    var cardPayment = cardService.addDebitCardPayment(request);
    cardPaymentsService.addTransactionToCardPayments(cardPayment.getId(), request.totalBuying());
    return ResponseEntity.status(HttpStatus.OK).body(new CardPaymentsResponse(cardPayment));
  }

  @PostMapping("/credit/{cardId}")
  public ResponseEntity payActualBilling(
      @PathVariable Long cardId, @RequestBody MonthRequest month) {
    var cards =
        cardPaymentsRepository
            .findByCardIdAndMonth(cardId, month.month())
            .orElseThrow(() -> new EntityNotFoundException("Nenhuma parecla pendente na fatura"));
    System.out.println(cards);
    cardService.payActualBilling(cards);
    return ResponseEntity.status(HttpStatus.valueOf(200)).build();
  }

  @Operation(summary = "getCardByID", description = "Retrieves card details by card ID")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Card found"),
    @ApiResponse(
        responseCode = "401",
        description = "Unauthorized - Invalid or missing authentication token"),
    @ApiResponse(responseCode = "404", description = "Card not found", content = @Content),
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
    @ApiResponse(responseCode = "200", description = "List of cards retrieved successfully"),
    @ApiResponse(
        responseCode = "401",
        description = "Unauthorized - Invalid or missing authentication token"),
  })
  @GetMapping("/list/{accountNumber}")
  public ResponseEntity getAllCardByAccount(@PathVariable String accountNumber) {
    var cards = cardService.getAllCardByAccountNumber(accountNumber);
    return ResponseEntity.status(HttpStatus.OK).body(cards);
  }

  @Operation(summary = "cardUpdate", description = "Updates card details by card ID")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Card updated successfully"),
    @ApiResponse(
        responseCode = "401",
        description = "Unauthorized - Invalid or missing authentication token"),
    @ApiResponse(responseCode = "404", description = "Card not found", content = @Content),
  })
  @PutMapping("/{id}")
  public ResponseEntity cardUpdate(@PathVariable Long id, @RequestBody @Valid CreditUpdate update) {
    var card = cardService.cardUpdate(update, id);
    return ResponseEntity.status(HttpStatus.OK).body(card);
  }

  @Operation(summary = "cardDelete", description = "Deletes a card by its ID")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Card deleted successfully"),
    @ApiResponse(
        responseCode = "401",
        description = "Unauthorized - Invalid or missing authentication token"),
    @ApiResponse(responseCode = "404", description = "Card not found", content = @Content),
  })
  @DeleteMapping("/{id}")
  public ResponseEntity cardDelete(@PathVariable Long id) {
    cardService.cardDelete(id);
    return ResponseEntity.status(HttpStatus.OK).body("card has been deleted");
  }
}
