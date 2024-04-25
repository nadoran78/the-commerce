package com.toy.thecommerce.domain.user.exception;

import com.toy.thecommerce.global.exception.CustomException;
import com.toy.thecommerce.global.type.ErrorCode;

public class UserException extends CustomException {

  public UserException(ErrorCode errorCode) {
    super(errorCode);
  }
}
