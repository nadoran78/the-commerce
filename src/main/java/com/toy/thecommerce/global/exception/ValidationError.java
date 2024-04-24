package com.toy.thecommerce.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.FieldError;

@Getter
@RequiredArgsConstructor
public class ValidationError {

  private final String field;
  private final String reason;

  public static ValidationError from(FieldError fieldError) {
    return new ValidationError(fieldError.getField(), fieldError.getDefaultMessage());
  }

}
