package com.bia.dev_bank.utils;

import lombok.experimental.UtilityClass;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@UtilityClass
public class PasswordUtils {
  private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

  public static String bcrypt(String rawPassword) {
    return encoder.encode(rawPassword);
  }
}
