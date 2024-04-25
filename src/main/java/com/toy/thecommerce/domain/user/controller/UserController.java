package com.toy.thecommerce.domain.user.controller;

import com.toy.thecommerce.domain.user.dto.SignUpRequest;
import com.toy.thecommerce.domain.user.service.UserService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

  private final UserService userService;

  @PostMapping("/join")
  public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpRequest request) {
    userService.signUp(request);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

}
