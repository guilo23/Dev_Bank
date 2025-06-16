package com.bia.dev_bank.controller;

import com.bia.dev_bank.dto.accountDTOs.AccountRequest;
import com.bia.dev_bank.dto.accountDTOs.AccountUpdate;
import com.bia.dev_bank.service.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/bia/account")
@Tag(name = "Account", description = "Endpoints for managing accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Operation(summary = "createAccount", description = "Creates a new bank account for an existing customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account sucessfully created", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping("/{customerId}")
    public ResponseEntity createAccount(@RequestBody AccountRequest request, @PathVariable Long customerId) {
        var account = accountService.createAccount(request, customerId);
        return ResponseEntity.ok().body(account.accountNumber() + " Parabéns sua conta foi criada com sucesso");
    }

    @Operation(summary = "getAccountByNumber", description = "Retrieves account details by account number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account found"),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content)
    })
    @GetMapping("/{accountNumber}")
    public ResponseEntity getAccountByNumber(@PathVariable String accountNumber) {
        var account = accountService.getAccountById(accountNumber);
        return ResponseEntity.ok().body(account);
    }

    @Operation(summary = "getAllAccountByCustomerID", description = "Retrieves all accounts associated with a specific customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accounts list retrieved successfully")
    })
    @GetMapping("/list/{customerId}")
    public ResponseEntity getAllAccountByCostumerId(@PathVariable Long customerId) {
        var accounts = accountService.getAllAccountByCostumerId(customerId);
        return ResponseEntity.ok().body(accounts);
    }

    @Operation(summary = "accountUpdate", description = "Updates the details of an existing account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account updated successfully"),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content)
    })
    @PutMapping("/{accountNumber}")
    public ResponseEntity accountUpdate(@PathVariable String accountNumber, @RequestBody AccountUpdate update) {
        accountService.accountUpdate(accountNumber, update);
        return ResponseEntity.ok().body("Parabéns sua BIA account foi atualizado com sucesso");
    }

    @Operation(summary = "accountDelete", description = "Deletes an account by its number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content)
    })
    @DeleteMapping("/{accountNumber}")
    public ResponseEntity accountDelete(@PathVariable String accountNumber) {
        accountService.accountDelete(accountNumber);
        return ResponseEntity.ok().body(":( Sua conta foi deletada, espero " +
                "que continue sendo nosso cliente");
    }
}
