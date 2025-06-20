package com.bia.dev_bank.serviceTest;

import com.bia.dev_bank.dto.transaction.TransactionRequest;
import com.bia.dev_bank.dto.transaction.TransactionResponse;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
class LoanPaymentsServiceTest {

  @InjectMocks private LoanPaymentsService loanPaymentsService;

  @Mock private LoanPaymentsRepository loanPaymentsRepository;

  @Mock private TransactionRepository transactionRepository;

  @Mock private AccountRepository accountRepository;

  @Mock private com.bia.dev_bank.utils.SecurityUtil securityUtil;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void shouldReturnLoanPaymentsById() {
    Customer customer = new Customer();
    customer.setId(1L);
    LoanPayments loanPayments = new LoanPayments();
    loanPayments.setLoanPaymentId(1L);
    loanPayments.setLoan(new com.bia.dev_bank.entity.Loan());
    loanPayments.getLoan().setCustomer(customer);

    when(loanPaymentsRepository.findById(1L)).thenReturn(Optional.of(loanPayments));
    when(securityUtil.getCurrentUserId()).thenReturn(1L);

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
  void shouldThrowAccessDeniedWhenCustomerIdMismatch() {
    Customer customer = new Customer();
    customer.setId(2L);
    LoanPayments loanPayments = new LoanPayments();
    loanPayments.setLoanPaymentId(1L);
    loanPayments.setLoan(new com.bia.dev_bank.entity.Loan());
    loanPayments.getLoan().setCustomer(customer);

    when(loanPaymentsRepository.findById(1L)).thenReturn(Optional.of(loanPayments));
    when(securityUtil.getCurrentUserId()).thenReturn(1L);

    assertThrows(AccessDeniedException.class, () -> loanPaymentsService.getLoanPaymentsById(1L));
  }

  @Test
  void shouldUpdatePaidAmountAndStatus() {
    Transaction t1 = new Transaction();
    t1.setAmount(BigDecimal.valueOf(50.0));
    Transaction t2 = new Transaction();
    t2.setAmount(BigDecimal.valueOf(100.0));

    Customer customer = new Customer();
    customer.setId(1L);
    LoanPayments payment = new LoanPayments();
    payment.setLoanPaymentId(1L);
    payment.setTransactions(List.of(t1, t2));
    payment.setPaymentAmount(BigDecimal.valueOf(150.0));
    payment.setScheduledPaymentDate(LocalDate.now().minusDays(1));
    payment.setLoan(new com.bia.dev_bank.entity.Loan());
    payment.getLoan().setCustomer(customer);

    when(loanPaymentsRepository.findById(1L)).thenReturn(Optional.of(payment));
    when(loanPaymentsRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
    when(securityUtil.getCurrentUserId()).thenReturn(1L);

    LoanPayments result = loanPaymentsService.updatePaidAmount(1L);

    assertEquals(BigDecimal.valueOf(150.0), result.getPaidAmount());
    assertEquals(PayedStatus.PAYED, result.getPayedStatus());
    assertEquals(LocalDate.now(), result.getPaidDate());
  }

  @Test
  void shouldAddTransactionToLoanPayment() {
    Long loanPaymentId = 1L;
    TransactionRequest request = new TransactionRequest(BigDecimal.valueOf(100.00), "12345");

    Customer customer = new Customer();
    customer.setId(1L);

    Account account = new Account();
    account.setAccountNumber("12345");
    account.setCustomer(customer);

    Account account1 = new Account();
    Customer customer1 = new Customer();
    customer1.setName("Maria da Silva");
    account1.setCustomer(customer1);

    LoanPayments loanPayments = new LoanPayments();
    loanPayments.setLoanPaymentId(loanPaymentId);
    loanPayments.setTransactions(new ArrayList<>());
    loanPayments.setLoan(new com.bia.dev_bank.entity.Loan());
    loanPayments.getLoan().setCustomer(customer);

    Transaction savedTransaction = new Transaction();
    savedTransaction.setAmount(BigDecimal.valueOf(100.00));
    savedTransaction.setOriginAccount(account);
    savedTransaction.setDestinyAccount(account1);
    savedTransaction.setTransactionDate(LocalDate.now());


    when(loanPaymentsRepository.findById(loanPaymentId)).thenReturn(Optional.of(loanPayments));
    when(accountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(account));
    when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);
    when(securityUtil.getCurrentUserId()).thenReturn(1L);


    TransactionResponse response = loanPaymentsService.addTransactionToLoanPayment(loanPaymentId, request);

    assertEquals(BigDecimal.valueOf(100.00), response.amount());
  }
}
