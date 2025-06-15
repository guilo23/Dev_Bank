package com.bia.dev_bank.serviceTest;


import com.bia.dev_bank.dto.transactionDTOs.TransactionRequest;
import com.bia.dev_bank.dto.transactionDTOs.TransactionResponse;
import com.bia.dev_bank.entity.Account;
import com.bia.dev_bank.entity.Customer;
import com.bia.dev_bank.entity.Transaction;
import com.bia.dev_bank.repository.AccountRepository;
import com.bia.dev_bank.repository.TransactionRepository;
import com.bia.dev_bank.service.AccountService;
import com.bia.dev_bank.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountService accountService;

    private Account originAccount;
    private Account destinyAccount;
    private Customer originCustomer;
    private Customer destinyCustomer;

    @BeforeEach
    void setup() {
        originCustomer = new Customer();
        originCustomer.setName("Carlos");

        destinyCustomer = new Customer();
        destinyCustomer.setName("Maria");

        originAccount = new Account();
        originAccount.setAccountNumber("123");
        originAccount.setCustomer(originCustomer);

        destinyAccount = new Account();
        destinyAccount.setAccountNumber("456");
        destinyAccount.setCustomer(destinyCustomer);
    }

    @Test
    void shouldCreateTransactionSuccessfully() {
        TransactionRequest request = new TransactionRequest(100.00, "456",null);
        Transaction savedTransaction = new Transaction(1L, 100.00, destinyAccount, originAccount, null, LocalDate.now());

        when(accountRepository.findByAccountNumber("123")).thenReturn(Optional.of(originAccount));
        when(accountRepository.findByAccountNumber("456")).thenReturn(Optional.of(destinyAccount));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);

        TransactionResponse response = transactionService.createTransaction(request, "123");

        assertEquals(100.00, response.amount());
        assertEquals("Maria", response.name());
        assertNotNull(response.transactionDate());

        verify(accountService).debit("123",100.00);
        verify(accountService).credit("456", 100.00);
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void shouldThrowExceptionWhenOriginAccountNotFound() {
        TransactionRequest request = new TransactionRequest(50.00, "456",null);

        when(accountRepository.findByAccountNumber("123")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            transactionService.createTransaction(request, "123");
        });
    }

    @Test
    void shouldReturnTransactionById() {
        Transaction transaction = new Transaction(1L,75.00, destinyAccount, originAccount, null, LocalDate.now());

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        TransactionResponse response = transactionService.getTransactionById(1L);

        assertEquals(75.00, response.amount());
        assertEquals("Maria", response.name());
    }

    @Test
    void shouldReturnTransactionsByAccountNumber() {
        Transaction transaction = new Transaction(1L,30.00, destinyAccount, originAccount, null, LocalDate.now());

        when(transactionRepository.findTransactionsByOriginAccountAccountNumber("123"))
                .thenReturn(List.of(transaction));

        List<TransactionResponse> responses = transactionService.getTransactionByAccountNumber("123");

        assertEquals(1, responses.size());
        assertEquals(30.00, responses.get(0).amount());
    }

    @Test
    void shouldReturnAllTransactions() {
        Transaction transaction = new Transaction(1L, 10.00, destinyAccount, originAccount, null, LocalDate.now());

        when(transactionRepository.findAll()).thenReturn(List.of(transaction));

        List<TransactionResponse> responses = transactionService.getAllTransactions();

        assertEquals(1, responses.size());
        assertEquals("Maria", responses.get(0).name());
    }

    @Test
    void shouldDeleteTransactionById() {
        transactionService.transactionDelete(1L);
        verify(transactionRepository).deleteById(1L);
    }
}
