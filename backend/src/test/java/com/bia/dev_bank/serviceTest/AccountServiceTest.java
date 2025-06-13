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


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
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
                List.of(),
                List.of(),
                500.0,
                LocalDate.now()
        );
    }
    @Test
     void shouldMakeDebitSucelly(){
        when(accountRepository.findByAccountNumber("12345678-9")).thenReturn(Optional.of(account));
        accountService.debit("12345678-9",200.0);
        assertEquals(300.0,account.getCurrentBalance());
        verify(accountRepository).save(account);
    }
    @Test
    void shouldThrowExceptionOnInsufficientBalance(){
        account.setCurrentBalance(100);
        when(accountRepository.findByAccountNumber("12345678-9")).thenReturn(Optional.of(account));
        assertThrows(IllegalArgumentException.class, () -> accountService.debit("12345678-9",200.0));
    }
    @Test
    void shouldMakeCreditSucelly(){
        when(accountRepository.findByAccountNumber("12345678-9")).thenReturn(Optional.of(account));
        accountService.credit("12345678-9",200.0);
        assertEquals(700,account.getCurrentBalance());
        verify(accountRepository).save(account);
    }
    @Test
    void ShouldCreateAccountSucelly(){
        var  request = new AccountRequest(AccountType.CHECKING,150.0);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        var response = accountService.createAccount(request,1L);
        assertEquals("João da Silva",response.customerName());
        assertEquals(AccountType.CHECKING,response.accountType());
        assertEquals(150,.0,response.currentBalance());

    }
    @Test
    void ShouldFindAccountById(){
        when(accountRepository.findByAccountNumber("12345678-9")).thenReturn(Optional.of(account));
        var response = accountService.getAccountById("12345678-9");
        assertEquals("João da Silva", response.customerName());
        assertEquals(500.0, response.currentBalance());
    }
    @Test
    void shouldThrowExceptionWhenAccountNotFound(){
        when(accountRepository.findByAccountNumber("0000000-0")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()-> accountService.getAccountById("0000000-0"));
    }
    @Test
    void ShouldUpdateAccountSucefully(){
        var update = new AccountUpdate(AccountType.SAVINGS,800.0);
        when(accountRepository.findByAccountNumber("12345678-9")).thenReturn(Optional.of(account));

        accountService.accountUpdate("12345678-9", update);

        assertEquals(AccountType.SAVINGS,account.getAccountType());
        assertEquals(800.0,account.getCurrentBalance());
        verify(accountRepository).save(account);
    }
    @Test
    void ShouldDeleteAccountSucelly(){
        when(accountRepository.findByAccountNumber("12345678-9")).thenReturn(Optional.of(account));
        accountService.accountDelete("12345678-9");
        verify(accountRepository).delete(account);

    }


}
