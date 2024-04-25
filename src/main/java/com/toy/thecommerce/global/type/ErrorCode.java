package com.toy.thecommerce.global.type;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

  JSON_EOF_ERROR(BAD_REQUEST, "잘못된 JSON 데이터입니다."),
  ARGUMENT_NOT_VALID(BAD_REQUEST, "잘못된 입력입니다."),
  HTTP_MESSAGE_NOT_READABLE(BAD_REQUEST, "HTTP 메시지를 읽을 수 없습니다."),
  INTERNAL_ERROR(INTERNAL_SERVER_ERROR, "처리되지 않은 에러가 발생했습니다."),
  DUPLICATE_USER_ID(BAD_REQUEST, "이미 존재하는 아이디입니다."),
  ALREADY_REGISTERED_USER(BAD_REQUEST, "이미 등록된 사용자입니다.(휴대폰 번호 중복)"),
  ;

  private final HttpStatus httpStatus;
  private final String message;
}
