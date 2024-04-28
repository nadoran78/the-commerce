package com.toy.thecommerce.domain.user.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.toy.thecommerce.domain.user.dao.UserRepository;
import com.toy.thecommerce.domain.user.dto.SignUpRequest;
import com.toy.thecommerce.domain.user.dto.UserInfoResponse;
import com.toy.thecommerce.domain.user.dto.UserInfoUpdateRequest;
import com.toy.thecommerce.domain.user.dto.UserListResponse;
import com.toy.thecommerce.domain.user.entity.User;
import com.toy.thecommerce.domain.user.exception.UserException;
import com.toy.thecommerce.global.type.ErrorCode;
import com.toy.thecommerce.global.utils.MaskingUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
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

  @Test
  void successGetUserList() {
    //given
    PageRequest pageRequest = PageRequest.of(0, 10, Direction.DESC, "createdAt");
    Page<User> users = makeUserPage(pageRequest);

    given(userRepository.findAll(any(Pageable.class))).willReturn(users);
    //when
    Page<UserListResponse> result = userService.getUserList(pageRequest);

    //then
    List<User> userList = users.getContent();
    List<UserListResponse> resultList = result.getContent();

    assertEquals(10, result.getTotalElements());
    assertEquals(userList.size(), resultList.size());
    checkUserAndUserListResponseField(userList.get(0), resultList.get(0));
    checkUserAndUserListResponseField(userList.get(1), resultList.get(1));
  }

  private void checkUserAndUserListResponseField(User user, UserListResponse response) {
    assertEquals(MaskingUtils.maskingUserIdOrName(user.getUserId()),
        response.getUserId());
    assertEquals(MaskingUtils.maskingUserIdOrName(user.getUsername()),
        response.getUsername());
    assertEquals(MaskingUtils.maskingUserIdOrName(user.getNickname()),
        response.getNickname());
    assertEquals(MaskingUtils.maskingPhone(user.getPhone()), response.getPhone());
    assertEquals(MaskingUtils.maskingEmail(user.getEmail()), response.getEmail());
  }

  private Page<User> makeUserPage(Pageable pageable) {
    List<User> userList = new ArrayList<>();
    for (int i = 0; i < 2; i++) {
      userList.add(User.builder()
          .userId("userId" + i)
          .password("password" + i)
          .nickname("nickname" + i)
          .username("username" + i)
          .phone("0101234123" + i)
          .email("email" + i + "@abcd.com")
          .build());
    }
    return new PageImpl<>(userList, pageable, 10);
  }

  @Test
  void successUpdateUserInfo() {
    //given
    UserInfoUpdateRequest request = UserInfoUpdateRequest.builder()
        .username("테스트")
        .nickname("nickname")
        .phone("010-9999-9999")
        .email("test@test.com")
        .build();
    User user = User.builder()
        .userId("userId")
        .nickname("nin")
        .username("use")
        .phone("phone")
        .email("email")
        .build();

    given(userRepository.findByUserId(anyString())).willReturn(Optional.of(user));
    given(userRepository.save(any(User.class))).will(returnsFirstArg());
    //when
    UserInfoResponse response = userService.updateUserInfo(request, "userId");
    //then
    assertEquals(user.getUserId(), response.getUserId());
    assertEquals("********", response.getPassword());
    assertEquals(request.getUsername(), response.getUsername());
    assertEquals(request.getNickname(), response.getNickname());
    assertEquals(request.getPhone(), response.getPhone());
    assertEquals(request.getEmail(), response.getEmail());
  }

  @Test
  void updateUserInfo_throwUserNotFoundException_whenUserIsNotExist() {
    //given
    UserInfoUpdateRequest request = UserInfoUpdateRequest.builder()
        .username("테스트")
        .nickname("nickname")
        .phone("010-9999-9999")
        .email("test@test.com")
        .build();

    given(userRepository.findByUserId(anyString())).willReturn(Optional.empty());
    //when
    UserException userException = assertThrows(UserException.class,
        () -> userService.updateUserInfo(request, "userId"));
    //then
    assertEquals(ErrorCode.USER_NOT_FOUND, userException.getErrorCode());
  }
}