package com.effcode.clean.me.rest;

import com.effcode.clean.me.handler.EmailHandler;
import com.effcode.clean.me.rest.dto.SendEmailRequestDto;
import com.effcode.clean.me.rest.dto.SendEmailResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/email")
@RequiredArgsConstructor
public class EmailRestController {

  private final EmailHandler emailHandler;

  @PostMapping("/send")
  public ResponseEntity<SendEmailResponseDto> send(
      @RequestBody @Valid SendEmailRequestDto requestDto) {

    boolean state = emailHandler.send(requestDto);

    HttpStatus status = state ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

    SendEmailResponseDto responseDto = SendEmailResponseDto.builder().isSuccess(state).build();

    return ResponseEntity.status(status).body(responseDto);
  }
}
