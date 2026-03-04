package com.effcode.clean.me.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@ToString
public class SendEmailRequestDto {
  @NotNull(message = "Please specify at least one recipient")
  @NotEmpty(message = "Please specify at least one recipient")
  @Size(max = 50, message = "The maximum number of recipients is 50")
  private List<@Email String> recipients;

  @Length(max = 255, message = "Subject must have maximum 255 symbols.")
  @NotBlank(message = "Subject must not be empty")
  private String subject;

  @Length(max = 65000, message = "The too Big. Maximum allowed body size is 65000")
  private String body;
}
