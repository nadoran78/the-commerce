package com.toy.thecommerce.global.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class PasswordEncoderConfigTest {
  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @Test
  void successEncode() {
    String password = "password";
    String encodedPassword = passwordEncoder.encode(password);

    assertNotEquals(password, encodedPassword);
    assertTrue(passwordEncoder.matches(password, encodedPassword));
  }
}