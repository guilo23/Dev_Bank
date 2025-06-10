package com.bia.dev_bank.service;

import com.bia.dev_bank.dto.CustomerRequest;
import com.bia.dev_bank.dto.CustomerResponse;
import com.bia.dev_bank.dto.CustomerUpdate;
import com.bia.dev_bank.entity.Customer;
import com.bia.dev_bank.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public CustomerResponse createCustomer(CustomerRequest request){
        var costumer = new Customer(
                null,
                request.name(),
                request.email(),
                request.password(),
                request.birthday(),
                request.CPF(),
                request.phoneNumber(),
                List.of()
        );
        customerRepository.save(costumer);
        return new CustomerResponse(costumer.getName(), costumer.getEmail(), costumer.getCPF(), costumer.getPhoneNumber());
    }
    public CustomerResponse getCostumerById(Long costumerId){
        var customer = customerRepository.findById(costumerId).orElseThrow(()
                -> new EntityNotFoundException("Customer not found with id: " + costumerId));

        return new CustomerResponse(customer.getName(),
                                    customer.getEmail(),
                                    customer.getCPF(),
                                    customer.getPhoneNumber());
    }
    public CustomerResponse customerUpdate(Long id, CustomerUpdate update){
       var costumer = customerRepository.findById(id).orElseThrow(()->
               new EntityNotFoundException("Customer not found with id: " + id));
       costumer.setEmail(update.email());
       costumer.setPassword(update.password());
       costumer.setPhoneNumber(update.phoneNumber());

       return new CustomerResponse(costumer.getName(), costumer.getEmail(), costumer.getCPF(), costumer.getPhoneNumber());
    }
    public void customerDelete(Long id){
        customerRepository.deleteById(id);
    }
}