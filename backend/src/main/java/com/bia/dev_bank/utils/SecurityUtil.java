package com.bia.dev_bank.utils;

import com.bia.dev_bank.entity.CustomUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {
  public Long getCurrentUserId() {
    var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (principal instanceof CustomUserDetails userDetails) {
      return userDetails.getId();
    }
    throw new IllegalStateException("Usuário não autenticado");
  }
}
