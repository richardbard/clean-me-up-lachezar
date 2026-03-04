package com.effcode.clean.me.rest.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class SendEmailResponseDto {
  private boolean isSuccess;
}
