package com.bia.dev_bank.service;

import com.bia.dev_bank.dto.report.StatementResponse;
import com.bia.dev_bank.dto.transaction.TransactionRequest;
import com.bia.dev_bank.dto.transaction.TransactionResponse;
import com.bia.dev_bank.entity.Account;
import com.bia.dev_bank.entity.Transaction;
import com.bia.dev_bank.repository.AccountRepository;
import com.bia.dev_bank.repository.TransactionRepository;
import com.bia.dev_bank.utils.SecurityUtil;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
  @Autowired private TransactionRepository transactionRepository;
  @Autowired private AccountRepository accountRepository;
  @Autowired private AccountService accountService;
  @Autowired private SecurityUtil securityUtil;

  public TransactionResponse createTransaction(
      TransactionRequest request, String originAccountNumber) {
    var custumerId = securityUtil.getCurrentUserId();
    Account account =
        accountRepository
            .findByAccountNumber(originAccountNumber)
            .orElseThrow(() -> new EntityNotFoundException("account not found"));

    if (!account.getCustomer().getId().equals(custumerId)) {
      throw new AccessDeniedException("permission denied");
    }
    var accountO =
        accountRepository
            .findByAccountNumber(originAccountNumber)
            .orElseThrow(() -> new EntityNotFoundException("account not found."));
    var accountD =
        accountRepository
            .findByAccountNumber(request.destinyAccountNumber())
            .orElseThrow(() -> new EntityNotFoundException("account not found."));
    var transaction =
        new Transaction(null, request.amount(), accountD, accountO, null, null, LocalDate.now());
    transactionRepository.save(transaction);

    accountService.debit(accountO.getAccountNumber(), transaction.getAmount());
    accountService.credit(accountD.getAccountNumber(), transaction.getAmount());

    return new TransactionResponse(
        transaction.getAmount(),
        transaction.getOriginAccount().getCustomer().getName(),
        transaction.getOriginAccount().getCustomer().getName(),
        transaction.getTransactionDate());
  }

  public List<StatementResponse> getStatementByAccountNumber(String accountNumber) {
    var custumerId = securityUtil.getCurrentUserId();
    Account account =
        accountRepository
            .findByAccountNumber(accountNumber)
            .orElseThrow(() -> new EntityNotFoundException("account not found"));

    if (!account.getCustomer().getId().equals(custumerId)) {
      throw new AccessDeniedException("permission denied");
    }

    List<Transaction> transactions =
        transactionRepository.findTransactionsByOriginAccountAccountNumber(accountNumber);

    return transactions.stream()
        .map(
            tx -> {
              String description;
              if (tx.getDestinyAccount() != null && tx.getDestinyAccount().getCustomer() != null) {
                description =
                    String.format(
                        new Locale("pt", "BR"),
                        "transaction of R$%.2f to %s",
                        tx.getAmount(),
                        tx.getDestinyAccount().getCustomer().getName());
              } else {
                description =
                    String.format(
                        new Locale("pt", "BR"),
                        "transaction of R$%.2f to unknow account",
                        tx.getAmount());
              }

              return new StatementResponse(
                  "transaction between accounts",
                  tx.getAmount(),
                  tx.getTransactionDate(),
                  description);
            })
        .toList();
  }

  public Page<Transaction> getTransactionsForAccount(String accountNumber, Pageable pageable) {
    return transactionRepository.findByAccountNumber(accountNumber, pageable);
  }

  public List<TransactionResponse> getAllTransactionsForAccount(String accountNumber) {
    List<Transaction> originTransactions =
        transactionRepository.findByOriginAccountAccountNumberOrderByTransactionDateDesc(
            accountNumber);

    List<Transaction> destinyTransactions =
        transactionRepository.findByDestinyAccountAccountNumberOrderByTransactionDateDesc(
            accountNumber);

    List<Transaction> allTransactions = new ArrayList<>();
    allTransactions.addAll(originTransactions);
    allTransactions.addAll(destinyTransactions);

    allTransactions.sort(Comparator.comparing(Transaction::getTransactionDate).reversed());

    return allTransactions.stream().map(TransactionResponse::new).collect(Collectors.toList());
  }

  public TransactionResponse getTransactionById(Long id) {
    var verify = transactionRepository.findById(id);
    var custumerId = securityUtil.getCurrentUserId();
    Account account =
        accountRepository
            .findByAccountNumber(verify.get().getOriginAccount().getAccountNumber())
            .orElseThrow(() -> new EntityNotFoundException("account not found"));

    if (!account.getCustomer().getId().equals(custumerId)) {
      throw new AccessDeniedException("permission denied");
    }
    var transaction =
        transactionRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new EntityNotFoundException("there is no transactions registered to this id"));
    return new TransactionResponse(
        transaction.getAmount(),
        transaction.getDestinyAccount().getCustomer().getName(),
        transaction.getOriginAccount().getCustomer().getName(),
        transaction.getTransactionDate());
  }

  public List<TransactionResponse> getTransactionByAccountNumber(String accountNumber) {
    var custumerId = securityUtil.getCurrentUserId();
    Account account =
        accountRepository
            .findByAccountNumber(accountNumber)
            .orElseThrow(() -> new EntityNotFoundException("account not found"));

    if (!account.getCustomer().getId().equals(custumerId)) {
      throw new AccessDeniedException("permission denied");
    }

    var transactions =
        transactionRepository.findTransactionsByOriginAccountAccountNumber(accountNumber);

    return transactions.stream().map(TransactionResponse::new).collect(Collectors.toList());
  }

  public List<TransactionResponse> getAllTransactions() {
    var transactions = transactionRepository.findAll();
    return transactions.stream().map(TransactionResponse::new).collect(Collectors.toList());
  }

  public void transactionDelete(Long id) {
    var verify = transactionRepository.findById(id);
    var custumerId = securityUtil.getCurrentUserId();
    Account account =
        accountRepository
            .findByAccountNumber(verify.get().getOriginAccount().getAccountNumber())
            .orElseThrow(() -> new EntityNotFoundException("account not found"));
    if (!account.getCustomer().getId().equals(custumerId)) {
      throw new AccessDeniedException("permission denied");
    }
    transactionRepository.deleteById(id);
  }
}
