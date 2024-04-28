package com.toy.thecommerce.global.exception;

import static com.toy.thecommerce.global.type.ErrorCode.ARGUMENT_NOT_VALID;
import static com.toy.thecommerce.global.type.ErrorCode.HTTP_MESSAGE_NOT_READABLE;
import static com.toy.thecommerce.global.type.ErrorCode.INTERNAL_ERROR;
import static com.toy.thecommerce.global.type.ErrorCode.JSON_EOF_ERROR;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.fasterxml.jackson.core.io.JsonEOFException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(CustomException e,
      HttpServletRequest request) {
    log.error("[CustomException] {} is occured. uri: {}", e.getErrorCode(),
        request.getRequestURI());

    return ResponseEntity
        .status(e.getErrorCode().getHttpStatus())
        .body(ErrorResponse.of(e.getErrorCode()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e, HttpServletRequest request) {
    log.error("MethodArgumentNotValidException is occurred. uri:{}",
        request.getRequestURI());

    return ResponseEntity
        .status(BAD_REQUEST)
        .body(ErrorResponse.of(e.getFieldErrors()));
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException e, HttpServletRequest request) {
    log.error("MethodArgumentTypeMismatchException is occurred. uri:{}",
        request.getRequestURI());

    return ResponseEntity
        .status(BAD_REQUEST)
        .body(ErrorResponse.of(ARGUMENT_NOT_VALID));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolationException(
      ConstraintViolationException e, HttpServletRequest request) {
    log.error("MethodArgumentTypeMismatchException is occurred. uri:{}",
        request.getRequestURI());

    return ResponseEntity
        .status(BAD_REQUEST)
        .body(ErrorResponse.of(ARGUMENT_NOT_VALID));
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException e, HttpServletRequest request) {
    log.error("HttpMessageNotReadableException is occurred. uri:{}",
        request.getRequestURI());

    if (e.getCause() instanceof JsonEOFException) {
      return ResponseEntity
          .status(BAD_REQUEST)
          .body(ErrorResponse.of(JSON_EOF_ERROR));
    }

    return ResponseEntity
        .status(BAD_REQUEST)
        .body(ErrorResponse.of(HTTP_MESSAGE_NOT_READABLE));
  }

  @ExceptionHandler(RuntimeException.class)
  private ResponseEntity<ErrorResponse> handleRuntimeException(
      RuntimeException e, HttpServletRequest request) {
    log.error("RuntimeException[{}] is occurred. uri:{}", e.getMessage(),
        request.getRequestURI());

    return ResponseEntity
        .status(INTERNAL_SERVER_ERROR)
        .body(ErrorResponse.of(INTERNAL_ERROR));
  }

}
