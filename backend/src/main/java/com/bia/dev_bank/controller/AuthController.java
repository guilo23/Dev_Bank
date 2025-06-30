package com.bia.dev_bank.controller;

import com.bia.dev_bank.dto.costumer.AuthRequest;
import com.bia.dev_bank.dto.costumer.AuthResponse;
import com.bia.dev_bank.security.CustomDetailService;
import com.bia.dev_bank.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
  @Autowired private AuthenticationManager authenticationManager;
  @Autowired private PasswordEncoder passwordEncoder;
  @Autowired private CustomDetailService userDetailsService;

  @Autowired private JwtUtil jwtUtil;

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
    Authentication auth =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.email(), request.password()));
    UserDetails userDetails = (UserDetails) auth.getPrincipal();
    var user = userDetailsService.loadUserByUsername(request.email());
    String token = jwtUtil.generateToken(userDetails);
    return ResponseEntity.ok(new AuthResponse(token));
  }
}
