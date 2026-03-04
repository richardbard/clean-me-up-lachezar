package com.effcode.clean.me.expection;

import com.effcode.clean.me.rest.dto.SendEmailResponseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class ErrorResponseDto extends SendEmailResponseDto {
  private String errorMessage;
}
