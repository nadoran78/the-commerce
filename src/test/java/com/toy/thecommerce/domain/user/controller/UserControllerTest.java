package com.toy.thecommerce.domain.user.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toy.thecommerce.domain.user.dto.SignUpRequest;
import com.toy.thecommerce.domain.user.dto.UserInfoResponse;
import com.toy.thecommerce.domain.user.dto.UserInfoUpdateRequest;
import com.toy.thecommerce.domain.user.dto.UserListResponse;
import com.toy.thecommerce.domain.user.service.UserService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(value = UserController.class)
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void successSignUp() throws Exception {
    //given
    SignUpRequest request = SignUpRequest.builder()
        .userId("thecommerce")
        .password("123Qwe!@#")
        .nickname("nickname")
        .username("더커머스")
        .phone("01000000000")
        .email("test@test.com")
        .build();
    //when & then
    mockMvc.perform(post("/api/user/join")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().is(201));

    ArgumentCaptor<SignUpRequest> captor = ArgumentCaptor.forClass(SignUpRequest.class);

    verify(userService).signUp(captor.capture());

    checkSignUpRequestEachField(request, captor.getValue());
  }

  @Test
  void signUp_throwArgumentNotValidException_whenInputInvalidArgument()
      throws Exception {
    //given
    SignUpRequest request = SignUpRequest.builder()
        .userId("thecommerce!@#")
        .password("123qwe!@#")
        .nickname("nickname!@#")
        .username("더커머스!@#")
        .phone("010-00-0000")
        .email("test@testcom")
        .build();
    //when & then
    ResultActions resultActions = mockMvc.perform(post("/api/user/join")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().is(400));
    checkArgumentNotValidEachField(resultActions);
  }

  private void checkArgumentNotValidEachField(ResultActions resultActions)
      throws Exception {
    resultActions
        .andExpect(jsonPath("$.errorCode").value("ARGUMENT_NOT_VALID"))
        .andExpect(jsonPath("$.message").value("잘못된 입력입니다."))
        .andExpect(jsonPath("$.errors[?(@.field == '%s')]", "userId").exists())
        .andExpect(jsonPath("$.errors[?(@.field == '%s')]", "password").exists())
        .andExpect(jsonPath("$.errors[?(@.field == '%s')]", "nickname").exists())
        .andExpect(jsonPath("$.errors[?(@.field == '%s')]", "username").exists())
        .andExpect(jsonPath("$.errors[?(@.field == '%s')]", "phone").exists())
        .andExpect(jsonPath("$.errors[?(@.field == '%s')]", "email").exists());
  }

  private void checkSignUpRequestEachField(SignUpRequest request, SignUpRequest captor) {
    assertEquals(request.getUserId(), captor.getUserId());
    assertEquals(request.getPassword(), captor.getPassword());
    assertEquals(request.getNickname(), captor.getNickname());
    assertEquals(request.getUsername(), captor.getUsername());
    assertEquals(request.getPhone(), captor.getPhone());
    assertEquals(request.getEmail(), captor.getEmail());
  }

  @Test
  void successGetUserList() throws Exception {
    // given
    Page<UserListResponse> responses = makePageUserListResponse();

    given(userService.getUserList(any(Pageable.class))).willReturn(responses);

    // when & then
    ResultActions resultActions = mockMvc.perform(get("/api/user/list")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print());

    checkUserListResponse(resultActions);
  }

  private Page<UserListResponse> makePageUserListResponse() {
    List<UserListResponse> userListResponseList = new ArrayList<>();
    for (int i = 1; i <= 3; i++) {
      userListResponseList.add(UserListResponse.builder()
          .userId("userId" + i)
          .nickname("nickname" + i)
          .username("username" + i)
          .phone("phone" + i)
          .email("email" + i)
          .build());
    }
    return new PageImpl<>(userListResponseList);
  }

  private void checkUserListResponse(ResultActions resultActions) throws Exception {
    for (int i = 1; i <= 3; i++) {
      resultActions
          .andExpect(jsonPath("$.content[" + (i - 1) + "].userId").value("userId" + i))
          .andExpect(
              jsonPath("$.content[" + (i - 1) + "].nickname").value("nickname" + i))
          .andExpect(
              jsonPath("$.content[" + (i - 1) + "].username").value("username" + i))
          .andExpect(jsonPath("$.content[" + (i - 1) + "].phone").value("phone" + i))
          .andExpect(jsonPath("$.content[" + (i - 1) + "].email").value("email" + i));

    }
  }

  @Test
  void getUserList_throwArgumentNotValidException_whenInputInvalidPage()
      throws Exception {
    //given
    //when & then
    mockMvc.perform(get("/api/user/list")
            .contentType(MediaType.APPLICATION_JSON)
            .param("page", "-1"))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.errorCode").value("ARGUMENT_NOT_VALID"))
        .andExpect(jsonPath("$.message").value("잘못된 입력입니다."))
        .andDo(print());

  }

  @Test
  void getUserList_throwArgumentNotValidException_whenInputInvalidSize()
      throws Exception {
    //given
    //when & then
    mockMvc.perform(get("/api/user/list")
            .contentType(MediaType.APPLICATION_JSON)
            .param("size", "-1"))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.errorCode").value("ARGUMENT_NOT_VALID"))
        .andExpect(jsonPath("$.message").value("잘못된 입력입니다."))
        .andDo(print());

  }

  @Test
  void getUserList_throwArgumentNotValidException_whenInputInvalidSort()
      throws Exception {
    //given
    //when & then
    mockMvc.perform(get("/api/user/list")
            .contentType(MediaType.APPLICATION_JSON)
            .param("sort", "nickname"))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.errorCode").value("ARGUMENT_NOT_VALID"))
        .andExpect(jsonPath("$.message").value("잘못된 입력입니다."))
        .andDo(print());

  }

  @Test
  void successUpdateUserInfo() throws Exception {
    //given
    UserInfoUpdateRequest request = UserInfoUpdateRequest.builder()
        .username("테스트")
        .nickname("nickname")
        .phone("010-9999-9999")
        .email("test@test.com")
        .build();

    UserInfoResponse response = UserInfoResponse.builder()
        .userId("userId")
        .password("********")
        .username("name")
        .nickname("name")
        .phone("010-1111-1111")
        .email("abcd@abcd.com")
        .build();

    given(userService.updateUserInfo(any(UserInfoUpdateRequest.class),
        anyString())).willReturn(response);
    //when & then
    ResultActions resultActions = mockMvc.perform(patch("/api/user/userId")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andDo(print());

    checkUpdateRequestAndResponseField(resultActions, response);
  }

  @Test
  void updateUserInfo_throwArgumentNotValidException_whenInputInvalidArgument() throws Exception {
    //given
    UserInfoUpdateRequest request = UserInfoUpdateRequest.builder()
        .username("테스트!@#")
        .nickname("nickname!@#")
        .phone("010-9999-9999111")
        .email("test@testcom")
        .build();

    //when & then
    ResultActions resultActions = mockMvc.perform(patch("/api/user/userId")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().is(400))
        .andDo(print());

    checkArgumentNotValidEachFieldUpdate(resultActions);
  }

  private void checkUpdateRequestAndResponseField(ResultActions resultActions,
      UserInfoResponse response) throws Exception {
    resultActions
        .andExpect(jsonPath("$.userId").value(response.getUserId()))
        .andExpect(jsonPath("$.password").value(response.getPassword()))
        .andExpect(jsonPath("$.username").value(response.getUsername()))
        .andExpect(jsonPath("$.nickname").value(response.getNickname()))
        .andExpect(jsonPath("$.phone").value(response.getPhone()))
        .andExpect(jsonPath("$.email").value(response.getEmail()));
  }

  private void checkArgumentNotValidEachFieldUpdate(ResultActions resultActions)
      throws Exception {
    resultActions
        .andExpect(jsonPath("$.errorCode").value("ARGUMENT_NOT_VALID"))
        .andExpect(jsonPath("$.message").value("잘못된 입력입니다."))
        .andExpect(jsonPath("$.errors[?(@.field == '%s')]", "nickname").exists())
        .andExpect(jsonPath("$.errors[?(@.field == '%s')]", "username").exists())
        .andExpect(jsonPath("$.errors[?(@.field == '%s')]", "phone").exists())
        .andExpect(jsonPath("$.errors[?(@.field == '%s')]", "email").exists());
  }
}