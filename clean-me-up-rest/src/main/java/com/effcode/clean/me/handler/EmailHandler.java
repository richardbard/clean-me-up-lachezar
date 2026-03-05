package com.effcode.clean.me.handler;

import com.effcode.clean.me.config.EmailProperties;
import com.effcode.clean.me.mapper.EmailMapper;
import com.effcode.clean.me.rest.dto.SendEmailRequestDto;
import com.effcode.clean.me.support.SmtpEmail;
import com.effcode.clean.me.support.SmtpHandler;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailHandler {

  private final SmtpHandler smtpHandler;
  private final EmailMapper emailMapper;
  private final EmailProperties emailProperties;

  public boolean send(SendEmailRequestDto requestDto) {
    log.info("Attempting to send email RequestDto:{}", requestDto);

    Set<String> uniqueRecipients = new LinkedHashSet<>(requestDto.getRecipients());

    SmtpEmail smtpEmail = emailMapper.toSmtpEmail(requestDto);

    if (uniqueRecipients.size() != requestDto.getRecipients().size()) {
      log.warn("Duplicate email recipients detected. Removing duplicates...");
      smtpEmail.adrs = uniqueRecipients.toArray(new String[0]);
    }

    smtpEmail.password = emailProperties.getPassword();
    smtpEmail.username = emailProperties.getUsername();
    smtpHandler.post(smtpEmail);
    return true;
  }
}
