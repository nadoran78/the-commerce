package com.toy.thecommerce.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInfoUpdateRequest {

  @NotNull(message = "반드시 값이 있어야 합니다.")
  @Pattern(regexp = "^(?=.*[가-힣a-zA-Z0-9])[가-힣a-zA-Z0-9]{2,10}$",
      message = "닉네임은 영문 대문자, 소문자, 한글, 숫자를 사용한 공백없는 6~10자여야 합니다.")
  @Schema(description = "닉네임: 영문 대문자, 소문자, 한글, 숫자를 사용한 공백없는 6~10자")
  private String nickname;

  @NotNull(message = "반드시 값이 있어야 합니다.")
  @Pattern(regexp = "^([a-zA-Z]+|[가-힣]+){2,10}$",
      message = "회원명은 영어 대,소문자만 작성하거나 한글로만 작성된 공백없는 2~10자여야 합니다.")
  @Schema(description = "회원 이름: 영어 대,소문자만 작성하거나 한글로만 작성된 공백없는 2~10자")
  private String username;

  @NotNull(message = "반드시 값이 있어야 합니다.")
  @Pattern(regexp = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$",
      message = "휴대전화 번호가 유효하지 않습니다.")
  @Schema(description = "휴대전화번호: '-'없거나 존재하는 11~12자리 숫자")
  private String phone;

  @NotNull(message = "반드시 값이 있어야 합니다.")
  @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
      message = "이메일 주소가 유효하지 않습니다.")
  @Schema(description = "이메일")
  private String email;

}
