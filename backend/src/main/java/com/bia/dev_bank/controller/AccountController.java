package com.bia.dev_bank.controller;

import com.bia.dev_bank.dto.accountDTOs.AccountRequest;
import com.bia.dev_bank.dto.accountDTOs.AccountUpdate;
import com.bia.dev_bank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bia/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/balanceIn/{accountNumber}")
    public ResponseEntity accountDeposit(@RequestBody AccountUpdate update,
                                         @PathVariable String accountNumber){
        accountService.accountDeposit(update, accountNumber);

      return  ResponseEntity.status(HttpStatus.ACCEPTED).body("o valor de R$" + update.currentBalance() +
              " foi depositado na sua conta com sucesso consulte seu extrato para mais detalhes.");
    }
    @PostMapping("/balanceOut/{accountNumber}")
    public ResponseEntity accountCashOut(@RequestBody AccountUpdate update,
                                         @PathVariable String accountNumber){
        accountService.accountCashOut(update, accountNumber);

        return  ResponseEntity.status(HttpStatus.ACCEPTED).body("o valor de R$" + update.currentBalance() +
                " foi sacado da sua conta com sucesso consulte seu extrato para mais detalhes.");
    }
    @PostMapping("/{customerId}")
    public ResponseEntity createAccount (@RequestBody AccountRequest request,@PathVariable Long customerId){
        var account = accountService.createAccount(request,customerId);
        return  ResponseEntity.ok().body(account.accountNumber() + " Parabéns sua conta foi criada com sucesso");
    }
    @GetMapping("/{accountNumber}")
    public ResponseEntity getAccountByNumber(@PathVariable String accountNumber){
        var account = accountService.getAccountById(accountNumber);
        return ResponseEntity.ok().body(account);
    }
    @GetMapping("/list/{customerId}")
    public ResponseEntity getAllAccountByCostumerId(@PathVariable Long customerId){
        var accounts = accountService.getAllAccountByCostumerId(customerId);
        return ResponseEntity.ok().body(accounts);
    }
    @PutMapping("/{accountNumber}")
    public ResponseEntity accountUpdate(@PathVariable String accountNumber,@RequestBody AccountUpdate update){
        accountService.accountUpdate(accountNumber,update);
        return ResponseEntity.ok().body("Parabéns sua BIA account foi atualizado com sucesso");
    }
    @DeleteMapping("/{accountNumber}")
    public ResponseEntity accountDelete(@PathVariable String accountNumber){
        accountService.accountDelete(accountNumber);
        return ResponseEntity.ok().body(":( Sua conta foi deletada, espero " +
                "que continue sendo nosso cliente");
    }
}
