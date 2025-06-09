package com.bia.dev_bank.controller;

import com.bia.dev_bank.dto.CustomerUpdate;
import com.bia.dev_bank.dto.CustomerRequest;
import com.bia.dev_bank.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("bia/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity createCustomer(@RequestBody CustomerRequest request) {
        var customer = customerService.createCustomer(request);
        return ResponseEntity.ok().body("Parabéns " + customer.name() +
                " seu cadastro foi feito com sucesso. Agora você é uma cliente da BIA");
    }

    @GetMapping("/{customerId}")
    public ResponseEntity customerById(@PathVariable Long customerId) {
        var customer = customerService.getCostumerById(customerId);
        return ResponseEntity.ok().body(customer);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity customerUpdate(@PathVariable Long customerId, @RequestBody CustomerUpdate request) {
        var updated = customerService.customerUpdate(customerId, request);
        return ResponseEntity.ok().body(updated);
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity customerDelete(@PathVariable Long customerId) {
        customerService.customerDelete(customerId);
        return ResponseEntity.noContent().build();
    }
}

