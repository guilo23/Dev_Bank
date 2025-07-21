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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

  private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

  @Autowired private AccountRepository accountRepository;
  @Autowired private CustomerRepository customerRepository;

  public void debit(String accountNumber, BigDecimal amount) {
    logger.info("Debiting {} from account {}", amount, accountNumber);
    var account =
        accountRepository
            .findByAccountNumber(accountNumber)
            .orElseThrow(() -> new EntityNotFoundException("account not found"));

    if (account.getCurrentBalance().compareTo(amount) == -1) {
      logger.warn("Account {} has insufficient balance for debit of {}", accountNumber, amount);
      throw new IllegalArgumentException("balance  not enough");
    }
    account.setCurrentBalance(account.getCurrentBalance().subtract(amount));
    accountRepository.save(account);
    logger.info("Successfully debited {} from account {}", amount, accountNumber);
  }

  public void credit(String accountNumber, BigDecimal amount) {
    logger.info("Crediting {} to account {}", amount, accountNumber);
    var account =
        accountRepository
            .findByAccountNumber(accountNumber)
            .orElseThrow(() -> new EntityNotFoundException("account not found"));

    account.setCurrentBalance(account.getCurrentBalance().add(amount));
    accountRepository.save(account);
    logger.info("Successfully credited {} to account {}", amount, accountNumber);
  }

  public AccountResponse accountDeposit(AccountUpdate update, String accountNumber) {
    logger.info("Processing deposit for account {}", accountNumber);
    var account =
        accountRepository
            .findByAccountNumber(accountNumber)
            .orElseThrow(() -> new EntityNotFoundException("account not found"));
    credit(account.getAccountNumber(), update.currentBalance());
    logger.info("Deposit of {} processed for account {}", update.currentBalance(), accountNumber);
    return new AccountResponse(
        accountNumber,
        account.getCustomer().getName(),
        account.getAccountType(),
        account.getCurrentBalance());
  }

  public AccountResponse accountCashOut(AccountUpdate update, String accountNumber) {
    logger.info("Processing cash out for account {}", accountNumber);
    var account =
        accountRepository
            .findByAccountNumber(accountNumber)
            .orElseThrow(() -> new EntityNotFoundException("account not found"));
    debit(account.getAccountNumber(), update.currentBalance());
    logger.info("Cash out of {} processed for account {}", update.currentBalance(), accountNumber);
    return new AccountResponse(
        accountNumber,
        account.getCustomer().getName(),
        account.getAccountType(),
        account.getCurrentBalance());
  }

  public AccountResponse createAccount(AccountRequest request, Long customerId) {
    logger.info("Creating account for customer {}", customerId);
    var customer =
        customerRepository
            .findById(customerId)
            .orElseThrow(() -> new EntityNotFoundException("customer not found  " + customerId));

    var accountNumberWithDv = generateAccountNumberWithCheckDigit();
    logger.info("Generated account number {}", accountNumberWithDv);

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
    logger.info("Account {} created successfully for customer {}", accountNumberWithDv, customerId);
    return new AccountResponse(
        account.getAccountNumber(),
        account.getCustomer().getName(),
        account.getAccountType(),
        account.getCurrentBalance());
  }

  public AccountResponse getAccountById(String accountNumber) {
    logger.info("Fetching account {}", accountNumber);
    var account =
        accountRepository
            .findByAccountNumber(accountNumber)
            .orElseThrow(() -> new EntityNotFoundException("account not found"));
    logger.info("Account {} found", accountNumber);

    return new AccountResponse(
        account.getAccountNumber(),
        account.getCustomer().getName(),
        account.getAccountType(),
        account.getCurrentBalance());
  }

  public List<AccountResponse> getAllAccountByCostumerId(Long customerId) {
    logger.info("Fetching all accounts for customer {}", customerId);
    var accounts = accountRepository.findAllAccountByCustomerId(customerId);
    logger.info("Found {} accounts for customer {}", accounts.size(), customerId);

    return accounts.stream().map(AccountResponse::new).collect(Collectors.toList());
  }

  public void accountUpdate(String accountNumber, AccountUpdate update) {
    logger.info("Updating account {}", accountNumber);
    var account =
        accountRepository
            .findByAccountNumber(accountNumber)
            .orElseThrow(() -> new EntityNotFoundException("account not found"));

    account.setCurrentBalance(update.currentBalance());
    account.setAccountType(update.AccountType());
    accountRepository.save(account);
    logger.info("Account {} updated successfully", accountNumber);
  }

  public void accountDelete(String accountNumber) {
    logger.info("Deleting account {}", accountNumber);
    var account =
        accountRepository
            .findByAccountNumber(accountNumber)
            .orElseThrow(() -> new EntityNotFoundException("account not found"));
    accountRepository.delete(account);
    logger.info("Account {} deleted successfully", accountNumber);
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
