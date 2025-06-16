package com.bia.dev_bank.controller;

import com.bia.dev_bank.dto.costumerDTOs.CustomerRequest;
import com.bia.dev_bank.dto.costumerDTOs.CustomerUpdate;
import com.bia.dev_bank.service.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("bia/customer")
@Tag(name = "Customer", description = "Endpoints for managing customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Operation(summary = "createCustomer", description = "Registers a new costumer in the BIA system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping
    public ResponseEntity createCustomer(@RequestBody CustomerRequest request) {
        var customer = customerService.createCustomer(request);
        return ResponseEntity.ok().body("Parabéns " + customer.name() +
                " seu cadastro foi feito com sucesso. Agora você é uma cliente da BIA");
    }

    @Operation(summary = "customerByID", description = "Retrieves customer details by customer ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer found"),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content)
    })
    @GetMapping("/{customerId}")
    public ResponseEntity customerById(@PathVariable Long customerId) {
        var customer = customerService.getCostumerById(customerId);
        return ResponseEntity.ok().body(customer);
    }

    @Operation(summary = "customerUpdate", description = "Updates customer details by customer ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer updated successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content)
    })
    @PutMapping("/{customerId}")
    public ResponseEntity customerUpdate(@PathVariable Long customerId, @RequestBody CustomerUpdate request) {
        var updated = customerService.customerUpdate(customerId, request);
        return ResponseEntity.ok().body(updated);
    }

    @Operation(summary = "customerDelete", description = "Deletes a customer by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content)
    })
    @DeleteMapping("/{customerId}")
    public ResponseEntity customerDelete(@PathVariable Long customerId) {
        customerService.customerDelete(customerId);
        return ResponseEntity.noContent().build();
    }
}
