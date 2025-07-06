package com.bia.dev_bank.service;

import com.bia.dev_bank.dto.costumer.CustomerRequest;
import com.bia.dev_bank.dto.costumer.CustomerResponse;
import com.bia.dev_bank.dto.costumer.CustomerUpdate;
import com.bia.dev_bank.entity.Customer;
import com.bia.dev_bank.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

  private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

  @Autowired private CustomerRepository customerRepository;
  @Autowired private PasswordEncoder passwordEncoder;

  public CustomerResponse createCustomer(CustomerRequest request) {
    logger.info("Creating a new customer");
    var customer =
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
    customerRepository.save(customer);
    logger.debug("Customer created with id: {}", customer.getId());
    return new CustomerResponse(
        customer.getId(),
        customer.getName(),
        customer.getEmail(),
        customer.getCPF(),
        customer.getPhoneNumber());
  }

  public CustomerResponse getCostumerById(Long costumerId) {
    logger.info("Getting customer by id: {}", costumerId);
    var customer =
        customerRepository
            .findById(costumerId)
            .orElseThrow(
                () -> {
                  logger.error("Customer not found with id: {}", costumerId);
                  return new EntityNotFoundException("customer not found with id: " + costumerId);
                });

    return new CustomerResponse(
        customer.getId(),
        customer.getName(),
        customer.getEmail(),
        customer.getCPF(),
        customer.getPhoneNumber());
  }

  public CustomerResponse customerUpdate(Long id, CustomerUpdate update) {
    logger.info("Updating customer with id: {}", id);
    var customer =
        customerRepository
            .findById(id)
            .orElseThrow(
                () -> {
                  logger.error("Customer not found with id: {}", id);
                  return new EntityNotFoundException("customer not found with id: " + id);
                });
    customer.setEmail(update.email());
    customer.setPassword(passwordEncoder.encode(update.password()));
    customer.setPhoneNumber(update.phoneNumber());
    customerRepository.save(customer);
    logger.debug("Customer updated with id: {}", customer.getId());
    return new CustomerResponse(
        customer.getId(),
        customer.getName(),
        customer.getEmail(),
        customer.getCPF(),
        customer.getPhoneNumber());
  }

  public void customerDelete(Long id) {
    logger.info("Deleting customer with id: {}", id);
    customerRepository.deleteById(id);
    logger.debug("Customer deleted with id: {}", id);
  }
}
