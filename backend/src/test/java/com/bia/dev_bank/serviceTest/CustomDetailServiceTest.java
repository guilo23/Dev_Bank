package com.bia.dev_bank.serviceTest;

import com.bia.dev_bank.entity.CustomUserDetails;
import com.bia.dev_bank.entity.Customer;
import com.bia.dev_bank.repository.CustomerRepository;
import com.bia.dev_bank.security.CustomDetailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomDetailServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomDetailService customDetailService;

    private Customer customer;

    @BeforeEach
    void setup() {
        customer = new Customer();
        customer.setId(1L);
        customer.setEmail("test@example.com");
        customer.setPassword("encryptedPassword");
        customer.setName("Test User");
    }

    @Test
    void shouldLoadUserByUsernameSuccessfully() {
        when(customerRepository.findByEmail("test@example.com")).thenReturn(Optional.of(customer));

        UserDetails userDetails = customDetailService.loadUserByUsername("test@example.com");

        assertNotNull(userDetails);
        assertTrue(userDetails instanceof CustomUserDetails);
        assertEquals(customer.getEmail(), userDetails.getUsername());
        verify(customerRepository).findByEmail("test@example.com");
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(customerRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        UsernameNotFoundException exception =
                assertThrows(
                        UsernameNotFoundException.class,
                        () -> customDetailService.loadUserByUsername("unknown@example.com"));

        assertEquals("Customer not found", exception.getMessage());
        verify(customerRepository).findByEmail("unknown@example.com");
    }
}