package com.bia.dev_bank.security;

import com.bia.dev_bank.entity.CustomUserDetails;
import com.bia.dev_bank.entity.Customer;
import com.bia.dev_bank.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomDetailService implements UserDetailsService {
  @Autowired private CustomerRepository customerRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Customer customer =
        customerRepository
            .findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Customer not found"));

    return new CustomUserDetails(customer);
  }
}
