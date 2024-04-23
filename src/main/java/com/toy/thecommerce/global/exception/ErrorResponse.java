package com.toy.thecommerce.global.exception;

import static com.toy.thecommerce.global.type.ErrorCode.ARGUMENT_NOT_VALID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.toy.thecommerce.global.type.ErrorCode;
import java.util.Collections;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.FieldError;

@Getter
@RequiredArgsConstructor
@Builder
public class ErrorResponse {

  private final ErrorCode errorCode;
  private final String message;

  @JsonInclude(Include.NON_NULL)
  private final List<Object> errors;

  public static ErrorResponse of(ErrorCode errorCode) {
    return ErrorResponse.builder()
        .errorCode(errorCode)
        .message(errorCode.getMessage())
        .build();
  }

  public static ErrorResponse of(List<FieldError> errors) {
    if (errors.isEmpty()) {
      return null;
    }

    List<Object> errorList = Collections.singletonList(
        errors.stream().map(ValidationError::from));

    return ErrorResponse.builder()
        .errorCode(ARGUMENT_NOT_VALID)
        .message(ARGUMENT_NOT_VALID.getMessage())
        .errors(errorList)
        .build();
  }

}
