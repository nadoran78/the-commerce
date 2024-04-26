package com.toy.thecommerce.domain.user.controller;

import com.toy.thecommerce.domain.user.dto.SignUpRequest;
import com.toy.thecommerce.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "user", description = "회원 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

  private final UserService userService;

  @PostMapping("/join")
  @Operation(summary = "회원가입", description = "회원가입을 통해 회원 정보를 입력하여 회원 정보를 저장합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "회원가입 성공"),
      @ApiResponse(responseCode = "400", description = "이미 등록된 사용자인 경우"),
      @ApiResponse(responseCode = "409", description = "중복된 회원 id인 경우")
  })
  public ResponseEntity<Void> signUp(
      @Parameter(description = "회원가입 요청 객체입니다.\n필드 관련 세부내용은 아래 Model을 확인해주세요.")
      @Valid @RequestBody SignUpRequest request) {
    userService.signUp(request);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

}
