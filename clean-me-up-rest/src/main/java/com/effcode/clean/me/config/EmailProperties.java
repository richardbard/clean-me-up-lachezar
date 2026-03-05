package com.effcode.clean.me.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "email.properties")
public class EmailProperties {
  private String username;
  private String password;
}
