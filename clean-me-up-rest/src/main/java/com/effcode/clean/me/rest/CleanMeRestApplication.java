package com.effcode.clean.me.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.effcode.clean.me.support.SmtpHandler;

@SpringBootApplication
public class CleanMeRestApplication {
    
    @Bean
    public SmtpHandler smtpHandler() {
        return new SmtpHandler();
    }

	public static void main(String[] args) {
		SpringApplication.run(CleanMeRestApplication.class, args);
	}
}
