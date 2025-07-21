package com.bia.dev_bank.controller;

import com.bia.dev_bank.dto.costumer.AuthRequest;
import com.bia.dev_bank.dto.costumer.AuthResponse;
import com.bia.dev_bank.dto.costumer.CustomerRequest;
import com.bia.dev_bank.security.CustomDetailService;
import com.bia.dev_bank.security.JwtUtil;
import com.bia.dev_bank.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication and registration")
public class AuthController {
  @Autowired private AuthenticationManager authenticationManager;
  @Autowired private PasswordEncoder passwordEncoder;
  @Autowired private CustomDetailService userDetailsService;
  @Autowired private CustomerService customerService;

  @Autowired private JwtUtil jwtUtil;

  @Operation(summary = "login", description = "Authenticates a user and returns a JWT token")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Authentication successful"),
    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
    @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content)
  })
  @PostMapping("/login")
  public ResponseEntity login(@RequestBody @Valid AuthRequest request) {
    try {
      Authentication auth =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(request.email(), request.password()));
      UserDetails userDetails = (UserDetails) auth.getPrincipal();
      String token = jwtUtil.generateToken(userDetails);
      return ResponseEntity.ok(new AuthResponse(token));
    } catch (AuthenticationException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
  }

  @Operation(summary = "register", description = "Register new customers")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Customer successfully created"),
    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
  })
  @PostMapping("/register")
  public ResponseEntity createCustomer(@RequestBody @Valid CustomerRequest request) {
    var customer = customerService.createCustomer(request);
    return ResponseEntity.ok().body(customer);
  }
}
