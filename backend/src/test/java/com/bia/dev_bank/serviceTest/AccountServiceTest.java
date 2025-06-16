package com.bia.dev_bank.serviceTest;

import com.bia.dev_bank.dto.accountDTOs.AccountRequest;
import com.bia.dev_bank.dto.accountDTOs.AccountResponse;
import com.bia.dev_bank.dto.accountDTOs.AccountUpdate;
import com.bia.dev_bank.entity.Account;
import com.bia.dev_bank.entity.Customer;
import com.bia.dev_bank.entity.enums.AccountType;
import com.bia.dev_bank.repository.AccountRepository;
import com.bia.dev_bank.repository.CustomerRepository;
import com.bia.dev_bank.service.AccountService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private AccountService accountService;

    private Customer customer;
    private Account account;
    private AccountType accountType;

    @BeforeEach
    void setup() {
        customer = new Customer(
                1L,
                "João da Silva",
                "joao@email.com",
                "senha123",
                "1985-01-01",
                "111.222.333-44",
                "11999999999",
                List.of()
        );


        account = new Account(
                "12345678-9",
                customer,
                AccountType.CHECKING,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                BigDecimal.valueOf(500.0),
                LocalDate.now()
        );
    }
    @Test
     void shouldMakeDebitSucelly(){
        when(accountRepository.findByAccountNumber("12345678-9")).thenReturn(Optional.of(account));
        accountService.debit("12345678-9",BigDecimal.valueOf(200.0));
        assertEquals(BigDecimal.valueOf(300.0),account.getCurrentBalance());
        verify(accountRepository).save(account);
    }
    @Test
    void shouldThrowExceptionOnInsufficientBalance(){
        account.setCurrentBalance(BigDecimal.valueOf(100));
        when(accountRepository.findByAccountNumber("12345678-9")).thenReturn(Optional.of(account));
        assertThrows(IllegalArgumentException.class, () -> accountService.debit("12345678-9",BigDecimal.valueOf(200.0)));
    }
    @Test
    void shouldMakeCreditSucelly(){
        when(accountRepository.findByAccountNumber("12345678-9")).thenReturn(Optional.of(account));
        accountService.credit("12345678-9",BigDecimal.valueOf(200.0));
        assertEquals(BigDecimal.valueOf(700.0),account.getCurrentBalance());
        verify(accountRepository).save(account);
    }
    @Test
    void ShouldCreateAccountSucelly(){
        var  request = new AccountRequest(AccountType.CHECKING,BigDecimal.valueOf(150.0));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        var response = accountService.createAccount(request,1L);
        assertEquals("João da Silva",response.customerName());
        assertEquals(AccountType.CHECKING,response.accountType());
        assertEquals(BigDecimal.valueOf(150.0),response.currentBalance());

    }
    @Test
    void ShouldFindAccountById(){
        when(accountRepository.findByAccountNumber("12345678-9")).thenReturn(Optional.of(account));
        var response = accountService.getAccountById("12345678-9");
        assertEquals("João da Silva", response.customerName());
        assertEquals(BigDecimal.valueOf(500.0), response.currentBalance());
    }
    @Test
    void shouldDepositToAccountSuccessfully() {
        String accountNumber = "000123";
        BigDecimal depositAmount = new BigDecimal("500.00");

        Customer customer = new Customer();
        customer.setName("Maria");

        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setCustomer(customer);
        account.setAccountType(AccountType.CHECKING);
        account.setCurrentBalance(new BigDecimal("1000.00"));

        AccountUpdate update = new AccountUpdate(AccountType.CHECKING, depositAmount);

        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));

        AccountResponse response = accountService.accountDeposit(update, accountNumber);

        assertEquals(accountNumber, response.accountNumber());
        assertEquals("Maria", response.customerName());
        assertEquals(AccountType.CHECKING, response.accountType());
        assertEquals(new BigDecimal("1500.00"), response.currentBalance()); // saldo atualizado
        verify(accountRepository).save(account);
    }
    @Test
    void shouldWithdrawFromAccountSuccessfully() {
        String accountNumber = "000123";
        BigDecimal withdrawAmount = new BigDecimal("300.00");

        Customer customer = new Customer();
        customer.setName("Carlos");

        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setCustomer(customer);
        account.setAccountType(AccountType.SAVINGS);
        account.setCurrentBalance(new BigDecimal("1000.00"));

        AccountUpdate update = new AccountUpdate(AccountType.SAVINGS, withdrawAmount);

        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));

        AccountResponse response = accountService.accountCashOut(update, accountNumber);

        assertEquals(accountNumber, response.accountNumber());
        assertEquals("Carlos", response.customerName());
        assertEquals(AccountType.SAVINGS, response.accountType());
        assertEquals(new BigDecimal("700.00"), response.currentBalance()); // saldo atualizado
        verify(accountRepository).save(account);
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFoundForDeposit() {
        String accountNumber = "999999";
        AccountUpdate update = new AccountUpdate(AccountType.CHECKING,new BigDecimal("100.00"));

        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> accountService.accountDeposit(update, accountNumber));
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFoundForWithdraw() {
        String accountNumber = "999999";
        AccountUpdate update = new AccountUpdate(AccountType.CHECKING,new BigDecimal("100.00"));

        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> accountService.accountCashOut(update, accountNumber));
    }
    @Test
    void shouldThrowExceptionWhenAccountNotFound(){
        when(accountRepository.findByAccountNumber("0000000-0")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()-> accountService.getAccountById("0000000-0"));
    }
    @Test
    void ShouldUpdateAccountSucefully(){
        var update = new AccountUpdate(AccountType.SAVINGS,BigDecimal.valueOf(800.0));
        when(accountRepository.findByAccountNumber("12345678-9")).thenReturn(Optional.of(account));

        accountService.accountUpdate("12345678-9", update);

        assertEquals(AccountType.SAVINGS,account.getAccountType());
        assertEquals(BigDecimal.valueOf(800.0),account.getCurrentBalance());
        verify(accountRepository).save(account);
    }
    @Test
    void ShouldDeleteAccountSucelly(){
        when(accountRepository.findByAccountNumber("12345678-9")).thenReturn(Optional.of(account));
        accountService.accountDelete("12345678-9");
        verify(accountRepository).delete(account);

    }


}
