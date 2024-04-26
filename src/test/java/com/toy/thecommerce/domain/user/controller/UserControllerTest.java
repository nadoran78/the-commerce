package com.toy.thecommerce.domain.user.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toy.thecommerce.domain.user.dto.SignUpRequest;
import com.toy.thecommerce.domain.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
}