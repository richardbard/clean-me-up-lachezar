package com.effcode.clean.me;

import com.effcode.clean.me.config.EmailProperties;
import com.effcode.clean.me.support.SmtpHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties({EmailProperties.class})
@SpringBootApplication
public class CleanMeRestApplication {

  public static void main(String[] args) {
    SpringApplication.run(CleanMeRestApplication.class, args);
  }
}
