package com.effcode.clean.me.mapper;

import com.effcode.clean.me.rest.dto.SendEmailRequestDto;
import com.effcode.clean.me.support.SmtpEmail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmailMapper {

  @Mapping(target = "adrs", source = "recipients")
  @Mapping(target = "subject", source = "subject")
  @Mapping(target = "content", source = "body")
  @Mapping(target = "username", ignore = true)
  @Mapping(target = "password", ignore = true)
  SmtpEmail toSmtpEmail(SendEmailRequestDto requestDto);
}
