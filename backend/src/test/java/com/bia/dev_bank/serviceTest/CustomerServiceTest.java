package com.bia.dev_bank.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.bia.dev_bank.dto.costumer.CustomerRequest;
import com.bia.dev_bank.dto.costumer.CustomerResponse;
import com.bia.dev_bank.dto.costumer.CustomerUpdate;
import com.bia.dev_bank.entity.Customer;
import com.bia.dev_bank.repository.CustomerRepository;
import com.bia.dev_bank.service.CustomerService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class CustomerServiceTest {

  @Mock private CustomerRepository customerRepository;

  @Mock private PasswordEncoder passwordEncoder;

  @InjectMocks private CustomerService customerService;

  private Customer customer;

  @BeforeEach
  void setup() {
    customer =
        new Customer(
            1L,
            "Maria Silva",
            "maria@email.com",
            "encodedSenha123",
            "USER",
            "1990-05-20",
            "123.456.789-00",
            "11999999999",
            List.of());
  }

  @Test
  void shouldCreateCustomerSuccessfully() {
    CustomerRequest request =
        new CustomerRequest(
            "Maria Silva",
            "maria@email.com",
            "senha123",
            "1990-05-20",
            "123.456.789-00",
            "11999999999");

    when(passwordEncoder.encode("senha123")).thenReturn("encodedSenha123");
    when(customerRepository.save(any(Customer.class))).thenReturn(customer);

    CustomerResponse response = customerService.createCustomer(request);

    assertEquals("Maria Silva", response.name());
    assertEquals("maria@email.com", response.email());
    assertEquals("123.456.789-00", response.CPF());
    assertEquals("11999999999", response.phoneNumber());
    verify(customerRepository).save(any(Customer.class));
  }

  @Test
  void shouldGetCustomerById() {
    when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

    CustomerResponse response = customerService.getCostumerById(1L);

    assertEquals("Maria Silva", response.name());
    verify(customerRepository).findById(1L);
  }

  @Test
  void shouldThrowExceptionWhenCustomerNotFoundById() {
    when(customerRepository.findById(99L)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> customerService.getCostumerById(99L));
  }

  @Test
  void shouldUpdateCustomerSuccessfully() {
    CustomerUpdate update = new CustomerUpdate("new@email.com", "newpassword", "11988888888");

    when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
    when(passwordEncoder.encode("newpassword")).thenReturn("encodedNewPassword");
    when(customerRepository.save(any(Customer.class))).thenReturn(customer);

    CustomerResponse response = customerService.customerUpdate(1L, update);

    assertEquals("new@email.com", response.email());
    assertEquals("11988888888", response.phoneNumber());
    verify(customerRepository).save(customer);
  }

  @Test
  void shouldDeleteCustomer() {
    doNothing().when(customerRepository).deleteById(1L);

    customerService.customerDelete(1L);

    verify(customerRepository).deleteById(1L);
  }
}
