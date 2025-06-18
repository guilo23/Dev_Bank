package com.bia.dev_bank.controller;

import com.bia.dev_bank.dto.account.AccountRequest;
import com.bia.dev_bank.dto.account.AccountUpdate;
import com.bia.dev_bank.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bia/account")
@Tag(name = "Account", description = "Endpoints for managing accounts")
public class AccountController {

  @Autowired private AccountService accountService;

  @Operation(
      summary = "accountDeposit",
      description = "Deposits the specified amount into the customer's account.")
  @ApiResponses({
    @ApiResponse(responseCode = "202", description = "Deposit successful"),
    @ApiResponse(responseCode = "400", description = "Invalid input data"),
  })
  @PostMapping("/balanceIn/{accountNumber}")
  public ResponseEntity accountDeposit(
      @RequestBody @Valid AccountUpdate update, @PathVariable String accountNumber) {
    accountService.accountDeposit(update, accountNumber);
    return ResponseEntity.status(HttpStatus.ACCEPTED)
        .body("the value " + update.currentBalance() + " has been deposited in your account");
  }

  @Operation(
      summary = "accountCashOut",
      description = "Withdraws the specified amount from the customer's account.")
  @ApiResponses({
    @ApiResponse(responseCode = "202", description = "Withdrawal successful"),
    @ApiResponse(responseCode = "400", description = "Invalid input data"),
  })
  @PostMapping("/balanceOut/{accountNumber}")
  public ResponseEntity accountCashOut(
      @RequestBody @Valid AccountUpdate update, @PathVariable String accountNumber) {
    accountService.accountCashOut(update, accountNumber);
    return ResponseEntity.status(HttpStatus.ACCEPTED)
        .body("the value " + update.currentBalance() + " has been debit of your account");
  }

  @Operation(
      summary = "createAccount",
      description = "Creates a new bank account for an existing customer")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Account sucessfully created",
        content = @Content(mediaType = "application/json")),
    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
  })
  @PostMapping("/{customerId}")
  public ResponseEntity createAccount(
      @RequestBody @Valid AccountRequest request, @PathVariable Long customerId) {
    var account = accountService.createAccount(request, customerId);
    return ResponseEntity.ok()
        .body(account.accountNumber() + " congratulation your account has been created");
  }

  @Operation(
      summary = "getAccountByNumber",
      description = "Retrieves account details by account number")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Account found"),
    @ApiResponse(responseCode = "404", description = "Account not found", content = @Content)
  })
  @GetMapping("/{accountNumber}")
  public ResponseEntity getAccountByNumber(@PathVariable String accountNumber) {
    var account = accountService.getAccountById(accountNumber);
    return ResponseEntity.ok().body(account);
  }

  @Operation(
      summary = "getAllAccountByCustomerID",
      description = "Retrieves all accounts associated with a specific customer")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Accounts list retrieved successfully")
  })
  @GetMapping("/list/{customerId}")
  public ResponseEntity getAllAccountByCostumerId(@PathVariable Long customerId) {
    var accounts = accountService.getAllAccountByCostumerId(customerId);
    return ResponseEntity.ok().body(accounts);
  }

  @Operation(summary = "accountUpdate", description = "Updates the details of an existing account")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Account updated successfully"),
    @ApiResponse(responseCode = "404", description = "Account not found", content = @Content)
  })
  @PutMapping("/{accountNumber}")
  public ResponseEntity accountUpdate(
      @PathVariable String accountNumber, @RequestBody @Valid AccountUpdate update) {
    accountService.accountUpdate(accountNumber, update);
    return ResponseEntity.ok().body("your account has been updated");
  }

  @Operation(summary = "accountDelete", description = "Deletes an account by its number")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Account deleted successfully"),
    @ApiResponse(responseCode = "404", description = "Account not found", content = @Content)
  })
  @DeleteMapping("/{accountNumber}")
  public ResponseEntity accountDelete(@PathVariable String accountNumber) {
    accountService.accountDelete(accountNumber);
    return ResponseEntity.ok().body(":( your account has been deleted");
  }
}
