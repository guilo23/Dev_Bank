package com.bia.dev_bank.service;

import com.bia.dev_bank.dto.AccountDTOs.AccountUpdate;
import com.bia.dev_bank.dto.transactionDTOs.TransactionRequest;
import com.bia.dev_bank.dto.transactionDTOs.TransactionResponse;
import com.bia.dev_bank.entity.Transaction;
import com.bia.dev_bank.repository.AccountRepository;
import com.bia.dev_bank.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountService accountService;

    public TransactionResponse createTransaction(TransactionRequest request, String originAccoutNumber) {
        var accountO = accountRepository.findByAccountNumber(originAccoutNumber)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada."));;
        var accountD = accountRepository.findByAccountNumber(request.destinyAccountnumber())
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada."));;
        var transaction = new Transaction(
                null,
                request.amount(),
                accountD,
                accountO,
                LocalDate.now()
        );
        transactionRepository.save(transaction);

       accountService.debit(accountO.getAccountNumber(),transaction.getAmount());
       accountService.credit(accountD.getAccountNumber(),transaction.getAmount());

        return new TransactionResponse(transaction.getAmount(),
                transaction.getDestinyAccount().getCustomer().getName(), transaction.getTransactionDate());
    }

    public TransactionResponse getTransactionById(Long id) {
        var transaction = transactionRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("não há nenhuma transação para esse id"));
        return new TransactionResponse(transaction.getAmount(),
                transaction.getDestinyAccount().getCustomer().getName(),
                transaction.getTransactionDate());
    }

    public List<TransactionResponse> getTransactionByAccountNumber(String accountNumber) {
        var transactions = transactionRepository.findTransactionsByOriginAccountAccountNumber(accountNumber);

        return transactions.stream().map(TransactionResponse::new).collect(Collectors.toList());

    }

    public List<TransactionResponse> getAllTransactions() {
        var transactions = transactionRepository.findAll();
        return transactions.stream().map(TransactionResponse::new).collect(Collectors.toList());
    }

    public void transactionDelete(Long id) {
        transactionRepository.deleteById(id);
    }
}
