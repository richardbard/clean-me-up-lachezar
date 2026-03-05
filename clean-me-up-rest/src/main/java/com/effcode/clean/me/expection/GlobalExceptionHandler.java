package com.effcode.clean.me.expection;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseBody
  public ResponseEntity<ErrorResponseDto> handleException(MethodArgumentNotValidException e) {

    var fieldError = e.getBindingResult().getFieldErrors().stream().findFirst().orElse(null);

    String fieldName = fieldError != null ? fieldError.getField() : null;
    String message = fieldError != null ? fieldError.getDefaultMessage() : null;

    ErrorResponseDto responseDto =
        ErrorResponseDto.builder()
            .isSuccess(false)
            .errorMessage("%s: %s".formatted(fieldName, message))
            .build();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
  }

  @ExceptionHandler(Exception.class)
  @ResponseBody
  public ResponseEntity<ErrorResponseDto> handleException(Exception e) {
    ErrorResponseDto responseDto =
        ErrorResponseDto.builder().isSuccess(false).errorMessage("Something Went Wrong!").build();

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
  }
}
