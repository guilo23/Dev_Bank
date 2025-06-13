package com.bia.dev_bank.serviceTest;

import com.bia.dev_bank.dto.loanDTOs.LoanRequest;
import com.bia.dev_bank.dto.loanDTOs.LoanResponse;
import com.bia.dev_bank.entity.Customer;
import com.bia.dev_bank.entity.Loan;
import com.bia.dev_bank.entity.enums.LoanType;
import com.bia.dev_bank.repository.CustomerRepository;
import com.bia.dev_bank.repository.LoanPaymentsRepository;
import com.bia.dev_bank.repository.LoanRepository;
import com.bia.dev_bank.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanServiceTest {

    private LoanRepository loanRepository;
    private CustomerRepository customerRepository;
    private LoanPaymentsRepository loanPaymentsRepository;
    private LoanService loanService;

    @BeforeEach
    void setUp() {
        loanRepository = mock(LoanRepository.class);
        customerRepository = mock(CustomerRepository.class);
        loanPaymentsRepository = mock(LoanPaymentsRepository.class);
        loanService = new LoanService(loanRepository, customerRepository, loanPaymentsRepository);
    }

    @Test
    void shouldCreateLoanSuccessfully() {
        // Arrange
        Long customerId = 1L;
        LoanRequest request = new LoanRequest(
                new BigDecimal("10000"),
                new BigDecimal("0.1"),
                12,
                LoanType.PERSONAL
        );

        Customer customer = new Customer();
        customer.setId(customerId);
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        ArgumentCaptor<Loan> loanCaptor = ArgumentCaptor.forClass(Loan.class);

        LoanResponse response = loanService.createLoan(request, customerId);


        verify(loanRepository, times(1)).save(loanCaptor.capture());
        verify(loanPaymentsRepository, times(1)).saveAll(any());

        Loan savedLoan = loanCaptor.getValue();

        assertEquals(request.loanAmount(), savedLoan.getLoanAmount());
        assertEquals(request.installments(), savedLoan.getInstallments());
        assertEquals(request.interestRate(), savedLoan.getInterestRate());
        assertEquals(request.loanType(), savedLoan.getLoanType());

        assertEquals(response.loanAmount(), request.loanAmount());
        assertEquals(response.installments(), request.installments());
    }

    @Test
    void shouldThrowExceptionWhenCustomerNotFound() {
        // Arrange
        Long customerId = 99L;
        LoanRequest request = new LoanRequest(
                new BigDecimal("5000"),
                new BigDecimal("0.15"),
                10,
                LoanType.PERSONAL
        );

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                loanService.createLoan(request, customerId));

        assertTrue(exception.getMessage().contains("Cliente não encontrado"));
    }

    @Test
    void shouldReturnAllLoans() {
        // Arrange
        Loan loan1 = new Loan(1L, LoanType.PERSONAL, LocalDate.now(), new BigDecimal("1000"), 10, null, null, new BigDecimal("0.1"), new Customer(), List.of());
        Loan loan2 = new Loan(2L, LoanType.BUSINESS, LocalDate.now(), new BigDecimal("2000"), 24, null, null, new BigDecimal("0.2"), new Customer(), List.of());

        when(loanRepository.findAll()).thenReturn(List.of(loan1, loan2));

        // Act
        List<LoanResponse> loans = loanService.findAllLoans();

        // Assert
        assertEquals(2, loans.size());
        assertEquals(loan1.getLoanAmount(), loans.get(0).loanAmount());
    }

    @Test
    void shouldReturnLoanById() {
        // Arrange
        Long loanId = 1L;
        Loan loan = new Loan(loanId, LoanType.PERSONAL, LocalDate.now(), new BigDecimal("5000"), 12, null, null, new BigDecimal("0.1"), new Customer(), List.of());

        when(loanRepository.findById(loanId)).thenReturn(Optional.of(loan));

        // Act
        LoanResponse response = loanService.findLoanById(loanId);

        // Assert
        assertEquals(loan.getLoanAmount(), response.loanAmount());
        assertEquals(loan.getInstallments(), response.installments());
    }

    @Test
    void shouldThrowExceptionWhenLoanNotFound() {
        // Arrange
        Long loanId = 99L;
        when(loanRepository.findById(loanId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> loanService.findLoanById(loanId));
    }
}