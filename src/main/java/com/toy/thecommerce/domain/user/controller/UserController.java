package com.toy.thecommerce.domain.user.controller;

import com.toy.thecommerce.domain.user.dto.SignUpRequest;
import com.toy.thecommerce.domain.user.dto.UserInfoResponse;
import com.toy.thecommerce.domain.user.dto.UserInfoUpdateRequest;
import com.toy.thecommerce.domain.user.dto.UserListResponse;
import com.toy.thecommerce.domain.user.service.UserService;
import com.toy.thecommerce.domain.user.type.UserListSort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "user", description = "회원 API")
@Validated
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

  @GetMapping("/list")
  @Operation(summary = "회원목록 조회", description = "가입한 회원정보를 조회하여 페이지 처리하여 반환합니다.\n"
      + "- 각 회원정보는 userId, nickname, username, phone, email을 마스킹 처리하여 반환합니다.\n"
      + "- 정렬기준 : 가입일순, 회원명순\n"
      + "- 가입일순으로 정렬 시 내림차순으로 반환\n"
      + "- 회원명순으로 정렬 시 오름차순으로 반환" )
  @ApiResponse(responseCode = "200", description = "회원목록 조회 성공")
  public Page<UserListResponse> getUserList(
      @Parameter(description = "요청 페이지 숫자를 입력하세요.")
      @Min(0) @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "1페이지 당 보여줄 Element 숫자를 입력하세요.")
      @Positive @RequestParam(defaultValue = "10") int size,
      @Parameter(description = "정렬 기준을 입력해 주세요.(createdAt 또는 username 대소문자 구분 불필요)\n")
      @RequestParam(defaultValue = "createdAt") UserListSort sort) {

    Direction direction = sort == UserListSort.CREATED_AT ? Direction.DESC : Direction.ASC;

    PageRequest pageRequest = PageRequest.of(page, size, direction, sort.getCode());

    return userService.getUserList(pageRequest);
  }


  @Operation(summary = "회원정보 수정", description = "가입한 회원의 회원정보를 수정합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "회원정보 수정 성공"),
      @ApiResponse(responseCode = "404", description = "저장된 회원정보가 없는 경우"),
  })
  @PatchMapping("/{userId}")
  public UserInfoResponse updateUserInfo(
      @Parameter(description = "회원수정 요청 객체입니다.\n필드 관련 세부내용은 아래 Model을 확인해주세요.")
      @Valid @RequestBody UserInfoUpdateRequest request,
      @Parameter(description = "로그인한 회원id입니다.")
      @PathVariable String userId) {
    return userService.updateUserInfo(request, userId);
  }

}
