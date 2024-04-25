package com.toy.thecommerce.domain.user.service;

import com.toy.thecommerce.domain.user.dao.UserRepository;
import com.toy.thecommerce.domain.user.dto.SignUpRequest;
import com.toy.thecommerce.domain.user.entity.User;
import com.toy.thecommerce.domain.user.exception.UserException;
import com.toy.thecommerce.global.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public void signUp(SignUpRequest request) {
    if (userRepository.findByUserId(request.getUserId()).isPresent()) {
      throw new UserException(ErrorCode.DUPLICATE_USER_ID);
    }

    if (userRepository.findByPhone(request.getPhone()).isPresent()) {
      throw new UserException(ErrorCode.ALREADY_REGISTERED_USER);
    }

    String encodedPassword = passwordEncoder.encode(request.getPassword());

    userRepository.save(User.from(request,encodedPassword));
  }

}
