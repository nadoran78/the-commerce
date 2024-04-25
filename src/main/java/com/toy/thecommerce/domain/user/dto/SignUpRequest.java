package com.toy.thecommerce.domain.user.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignUpRequest {

  @NotNull(message = "반드시 값이 있어야 합니다.")
  @Pattern(regexp = "^(?=.*[A-Za-z])[a-zA-Z0-9]{6,12}$",
      message = "ID는 영문 대,소문자 또는 영문 대,소문자, 숫자 조합 6~12자여야 합니다.")
  private final String userId;

  @NotNull(message = "반드시 값이 있어야 합니다.")
  @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-])[^\\s].{8,16}$",
      message = "비밀번호는 8~16자로 영문 대문자, 소문자, 숫자, 특수문자가 모두 1개 이상 포함되어야 합니다.")
  private final String password;

  @NotNull(message = "반드시 값이 있어야 합니다.")
  @Pattern(regexp = "^(?=.*[가-힣a-zA-Z0-9])[가-힣a-zA-Z0-9]{2,10}$",
      message = "닉네임은 영문 대문자, 소문자, 한글, 숫자를 사용한 공백없는 6~10자여야 합니다.")
  private final String nickname;

  @NotNull(message = "반드시 값이 있어야 합니다.")
  @Pattern(regexp = "^([a-zA-Z]+|[가-힣]+){2,10}$",
      message = "닉네임은 영어 대,소문자만 작성하거나 한글로만 작성된 공백없는 2~10자여야 합니다.")
  private final String username;

  @NotNull(message = "반드시 값이 있어야 합니다.")
  @Pattern(regexp = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$",
      message = "휴대전화 번호가 유효하지 않습니다.")
  private final String phone;

  @NotNull(message = "반드시 값이 있어야 합니다.")
  @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$\n",
      message = "이메일 주소가 유효하지 않습니다.")
  private final String email;

}
