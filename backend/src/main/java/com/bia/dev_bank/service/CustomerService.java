package com.bia.dev_bank.service;

import com.bia.dev_bank.dto.costumer.CustomerRequest;
import com.bia.dev_bank.dto.costumer.CustomerResponse;
import com.bia.dev_bank.dto.costumer.CustomerUpdate;
import com.bia.dev_bank.entity.Customer;
import com.bia.dev_bank.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
  @Autowired private CustomerRepository customerRepository;
  @Autowired private PasswordEncoder passwordEncoder;

  public CustomerResponse createCustomer(CustomerRequest request) {
    var costumer =
        new Customer(
            null,
            request.name(),
            request.email(),
            passwordEncoder.encode(request.password()),
            "USER",
            request.birthday(),
            request.CPF(),
            request.phoneNumber(),
            List.of());
    customerRepository.save(costumer);
    return new CustomerResponse(
        costumer.getName(), costumer.getEmail(), costumer.getCPF(), costumer.getPhoneNumber());
  }

  public CustomerResponse getCostumerById(Long costumerId) {
    var customer =
        customerRepository
            .findById(costumerId)
            .orElseThrow(
                () -> new EntityNotFoundException("customer not found with id: " + costumerId));

    return new CustomerResponse(
        customer.getName(), customer.getEmail(), customer.getCPF(), customer.getPhoneNumber());
  }

  public CustomerResponse customerUpdate(Long id, CustomerUpdate update) {
    var customer =
        customerRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("customer not found with id: " + id));
    customer.setEmail(update.email());
    customer.setPassword(passwordEncoder.encode(update.password()));
    customer.setPhoneNumber(update.phoneNumber());
    customerRepository.save(customer);

    return new CustomerResponse(
        customer.getName(), customer.getEmail(), customer.getCPF(), customer.getPhoneNumber());
  }

  public void customerDelete(Long id) {
    customerRepository.deleteById(id);
  }
}
