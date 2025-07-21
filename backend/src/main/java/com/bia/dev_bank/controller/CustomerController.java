package com.bia.dev_bank.controller;

import com.bia.dev_bank.dto.costumer.CustomerRequest;
import com.bia.dev_bank.dto.costumer.CustomerUpdate;
import com.bia.dev_bank.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("bia/customer")
@Tag(name = "Customer", description = "Endpoints for managing customers")
public class CustomerController {

  private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

  @Autowired private CustomerService customerService;

  @Operation(summary = "createCustomer", description = "Registers a new costumer in the BIA system")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Customer successfully created"),
    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
  })
  @PostMapping
  public ResponseEntity createCustomer(@RequestBody @Valid CustomerRequest request) {
    logger.info("Request received to create customer");
    var customer = customerService.createCustomer(request);
    return ResponseEntity.ok().body(customer);
  }

  @Operation(summary = "customerByID", description = "Retrieves customer details by customer ID")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Customer found"),
    @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content)
  })
  @PreAuthorize("#account.customer.id == principal.id")
  @GetMapping("/{customerId}")
  public ResponseEntity customerById(@PathVariable Long customerId) {
    logger.info("Request received to get customer {}", customerId);
    var customer = customerService.getCostumerById(customerId);
    return ResponseEntity.ok().body(customer);
  }

  @Operation(summary = "customerUpdate", description = "Updates customer details by customer ID")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Customer updated successfully"),
    @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content)
  })
  @PreAuthorize("#account.customer.id == principal.id")
  @PutMapping("/{customerId}")
  public ResponseEntity customerUpdate(
      @PathVariable Long customerId, @RequestBody @Valid CustomerUpdate request) {
    logger.info("Request received to update customer {}", customerId);
    var updated = customerService.customerUpdate(customerId, request);
    return ResponseEntity.ok().body(updated);
  }

  @Operation(summary = "customerDelete", description = "Deletes a customer by ID")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
    @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content)
  })
  @PreAuthorize("#account.customer.id == principal.id")
  @DeleteMapping("/{customerId}")
  public ResponseEntity customerDelete(@PathVariable Long customerId) {
    logger.info("Request received to delete customer {}", customerId);
    customerService.customerDelete(customerId);
    return ResponseEntity.noContent().build();
  }
}
