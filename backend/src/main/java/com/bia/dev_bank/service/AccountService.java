package com.bia.dev_bank.service;

import com.bia.dev_bank.dto.account.AccountRequest;
import com.bia.dev_bank.dto.account.AccountResponse;
import com.bia.dev_bank.dto.account.AccountUpdate;
import com.bia.dev_bank.entity.Account;
import com.bia.dev_bank.repository.AccountRepository;
import com.bia.dev_bank.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

  @Autowired private AccountRepository accountRepository;
  @Autowired private CustomerRepository customerRepository;

  public void debit(String accountNumber, BigDecimal amount) {
    var account =
        accountRepository
            .findByAccountNumber(accountNumber)
            .orElseThrow(() -> new EntityNotFoundException("account not found"));

    if (account.getCurrentBalance().compareTo(amount) == -1) {
      throw new IllegalArgumentException("balance  not enough");
    }
    account.setCurrentBalance(account.getCurrentBalance().subtract(amount));
    accountRepository.save(account);
  }

  public void credit(String accountNumber, BigDecimal amount) {
    var account =
        accountRepository
            .findByAccountNumber(accountNumber)
            .orElseThrow(() -> new EntityNotFoundException("account not found"));

    account.setCurrentBalance(account.getCurrentBalance().add(amount));
    accountRepository.save(account);
  }

  public AccountResponse accountDeposit(AccountUpdate update, String accountNumber) {
    var account =
        accountRepository
            .findByAccountNumber(accountNumber)
            .orElseThrow(() -> new EntityNotFoundException("account not found"));
    credit(account.getAccountNumber(), update.currentBalance());
    return new AccountResponse(
        accountNumber,
        account.getCustomer().getName(),
        account.getAccountType(),
        account.getCurrentBalance());
  }

  public AccountResponse accountCashOut(AccountUpdate update, String accountNumber) {
    var account =
        accountRepository
            .findByAccountNumber(accountNumber)
            .orElseThrow(() -> new EntityNotFoundException("account not found"));
    debit(account.getAccountNumber(), update.currentBalance());
    return new AccountResponse(
        accountNumber,
        account.getCustomer().getName(),
        account.getAccountType(),
        account.getCurrentBalance());
  }

  public AccountResponse createAccount(AccountRequest request, Long customerId) {
    var customer =
        customerRepository
            .findById(customerId)
            .orElseThrow(() -> new EntityNotFoundException("customer not found  " + customerId));

    var accountNumberWithDv = generateAccountNumberWithCheckDigit();

    var account =
        new Account(
            accountNumberWithDv,
            customer,
            request.accountType(),
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>(),
            request.currentBalance(),
            LocalDate.now());
    accountRepository.save(account);
    return new AccountResponse(
        account.getAccountNumber(),
        account.getCustomer().getName(),
        account.getAccountType(),
        account.getCurrentBalance());
  }

  public AccountResponse getAccountById(String accountNumber) {
    var account =
        accountRepository
            .findByAccountNumber(accountNumber)
            .orElseThrow(() -> new EntityNotFoundException("account not found"));

    return new AccountResponse(
        account.getAccountNumber(),
        account.getCustomer().getName(),
        account.getAccountType(),
        account.getCurrentBalance());
  }

  public List<AccountResponse> getAllAccountByCostumerId(Long customerId) {
    var accounts = accountRepository.findAllAccountByCustomerId(customerId);

    return accounts.stream().map(AccountResponse::new).collect(Collectors.toList());
  }

  public void accountUpdate(String accountNumber, AccountUpdate update) {
    var account =
        accountRepository
            .findByAccountNumber(accountNumber)
            .orElseThrow(() -> new EntityNotFoundException("account not found"));

    account.setCurrentBalance(update.currentBalance());
    account.setAccountType(update.AccountType());
    accountRepository.save(account);
  }

  public void accountDelete(String accountNumber) {
    var account =
        accountRepository
            .findByAccountNumber(accountNumber)
            .orElseThrow(() -> new EntityNotFoundException("account not found"));
    accountRepository.delete(account);
  }

  public static String generateAccountNumberWithCheckDigit() {
    var accountNumber = String.format("%08d", new Random().nextInt(100_000_000));

    int[] weights = {9, 8, 7, 6, 5, 4, 3, 2};
    int sum = 0;

    for (int i = 0; i < accountNumber.length(); i++) {
      int digit = Character.getNumericValue(accountNumber.charAt(i));
      sum += digit * weights[i];
    }

    int remainder = sum % 11;
    int checkDigit = (remainder < 2) ? 0 : 11 - remainder;

    return accountNumber + "-" + checkDigit;
  }
}
