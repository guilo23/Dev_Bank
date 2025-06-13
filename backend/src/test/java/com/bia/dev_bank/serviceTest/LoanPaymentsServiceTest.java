package com.bia.dev_bank.serviceTest;


import com.bia.dev_bank.dto.transactionDTOs.TransactionRequest;
import com.bia.dev_bank.dto.transactionDTOs.TransactionResponse;
import com.bia.dev_bank.entity.Account;
import com.bia.dev_bank.entity.Customer;
import com.bia.dev_bank.entity.LoanPayments;
import com.bia.dev_bank.entity.Transaction;
import com.bia.dev_bank.entity.enums.PayedStatus;
import com.bia.dev_bank.repository.AccountRepository;
import com.bia.dev_bank.repository.LoanPaymentsRepository;
import com.bia.dev_bank.repository.TransactionRepository;
import com.bia.dev_bank.service.LoanPaymentsService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanPaymentsServiceTest {

    @InjectMocks
    private LoanPaymentsService loanPaymentsService;

    @Mock
    private LoanPaymentsRepository loanPaymentsRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnLoanPaymentsById() {
        LoanPayments loanPayments = new LoanPayments();
        loanPayments.setLoanPaymentId(1L);

        when(loanPaymentsRepository.findById(1L)).thenReturn(Optional.of(loanPayments));

        LoanPayments result = loanPaymentsService.getLoanPaymentsById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getLoanPaymentId());
    }

    @Test
    void shouldThrowWhenLoanPaymentNotFound() {
        when(loanPaymentsRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> loanPaymentsService.getLoanPaymentsById(1L));
    }

    @Test
    void shouldUpdatePaidAmountAndStatus() {
        Transaction t1 = new Transaction();
        t1.setAmount(50.0);
        Transaction t2 = new Transaction();
        t2.setAmount(100.0);

        LoanPayments payment = new LoanPayments();
        payment.setLoanPaymentId(1L);
        payment.setTransactions(List.of(t1, t2));
        payment.setPaymentAmount(new BigDecimal("150.00"));
        payment.setScheduledPaymentDate(LocalDate.now().minusDays(1));

        when(loanPaymentsRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(loanPaymentsRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        LoanPayments result = loanPaymentsService.updatePaidAmount(1L);

        assertEquals(new BigDecimal("150"), result.getPaidAmount());
        assertEquals(PayedStatus.PAYED, result.getPayedStatus());
        assertEquals(LocalDate.now(), result.getPaidDate());
    }

    @Test
    void shouldAddTransactionToLoanPayment() {
        // Arrange
        Long loanPaymentId = 1L;
        TransactionRequest request = new TransactionRequest(100.00, "12345",String.valueOf(1L));

        // mock da conta e customer
        Customer customer = new Customer();
        customer.setName("João da Silva");

        Account account = new Account();
        account.setAccountNumber("12345");
        account.setCustomer(customer);

        LoanPayments loanPayments = new LoanPayments();
        loanPayments.setLoanPaymentId(loanPaymentId);
        loanPayments.setTransactions(new ArrayList<>());

        // comportamento dos mocks
        when(loanPaymentsRepository.findById(loanPaymentId)).thenReturn(Optional.of(loanPayments));
        when(accountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(account));

        // Simular salvamento da transação
        Transaction savedTransaction = new Transaction();
        savedTransaction.setAmount(100.00);
        savedTransaction.setDestinyAccount(account);
        savedTransaction.setTransactionDate(LocalDate.now());

        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);

        // Act
        TransactionResponse response = loanPaymentsService.addTransactionToLoanPayment(loanPaymentId, request);

        // Assert
        assertEquals(100.00, response.amount());
        assertEquals("João da Silva", response.name());
    }
}
