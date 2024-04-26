package com.toy.thecommerce.domain.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.toy.thecommerce.domain.user.dao.UserRepository;
import com.toy.thecommerce.domain.user.dto.SignUpRequest;
import com.toy.thecommerce.domain.user.entity.User;
import com.toy.thecommerce.domain.user.exception.UserException;
import com.toy.thecommerce.global.type.ErrorCode;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserRepository userRepository;
  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private UserService userService;

  @Test
  void successSignUp() {
    //given
    SignUpRequest request = SignUpRequest.builder()
        .userId("thecommerce")
        .password("123Qwe!@#")
        .nickname("nickname")
        .username("더커머스")
        .phone("01000000000")
        .email("test@test.com")
        .build();

    given(userRepository.findByUserId(anyString())).willReturn(Optional.empty());
    given(userRepository.findByPhone(anyString())).willReturn(Optional.empty());
    given(passwordEncoder.encode(anyString())).willReturn("encodedPassword");

    //when
    userService.signUp(request);

    //then
    ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
    verify(userRepository).save(captor.capture());

    assertEqualSignUpRequestAndUserEntityField(request, captor.getValue());
  }

  @Test
  void signUp_throwUserException_whenInputDuplicateUserId() {
    //given
    SignUpRequest request = SignUpRequest.builder()
        .userId("thecommerce")
        .password("123Qwe!@#")
        .nickname("nickname")
        .username("더커머스")
        .phone("01000000000")
        .email("test@test.com")
        .build();

    given(userRepository.findByUserId(anyString())).willReturn(
        Optional.of(mock(User.class)));
    //when
    UserException userException = assertThrows(UserException.class,
        () -> userService.signUp(request));
    //then
    assertEquals(ErrorCode.DUPLICATE_USER_ID, userException.getErrorCode());
  }

  @Test
  void signUp_throwUserException_whenRequestByAlreadyRegisteredUser() {
    //given
    SignUpRequest request = SignUpRequest.builder()
        .userId("thecommerce")
        .password("123Qwe!@#")
        .nickname("nickname")
        .username("더커머스")
        .phone("01000000000")
        .email("test@test.com")
        .build();

    given(userRepository.findByUserId(anyString())).willReturn(Optional.empty());
    given(userRepository.findByPhone(anyString())).willReturn(
        Optional.of(mock(User.class)));
    //when
    UserException userException = assertThrows(UserException.class,
        () -> userService.signUp(request));
    //then
    assertEquals(ErrorCode.ALREADY_REGISTERED_USER, userException.getErrorCode());

  }

  private void assertEqualSignUpRequestAndUserEntityField(SignUpRequest request,
      User user) {
    assertEquals(request.getUserId(), user.getUserId());
    assertEquals("encodedPassword", user.getPassword());
    assertEquals(request.getNickname(), user.getNickname());
    assertEquals(request.getUsername(), user.getUsername());
    assertEquals(request.getPhone(), user.getPhone());
    assertEquals(request.getEmail(), user.getEmail());
  }
}