package com.effcode.clean.me.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.effcode.clean.me.support.SmtpEmail;
import com.effcode.clean.me.support.SmtpHandler;

@Component
public class EmailHandler {
    
    @Autowired
    SmtpHandler smtpHandler;
    
    Logger log = LoggerFactory.getLogger(EmailHandler.class);
    
    public boolean send(String adr, String subject, String content) {
        log.debug("Adr: " + adr);
        log.debug("Subject: " + subject);
        log.debug("Content: " + content);
        if(subject == null) {
            log.error("Subject is null");
            return false;
        }
        if(content.length() > 65000) {
            log.error("Content to BIG: " + content.length());
            return false;
        }
        SmtpEmail smtpEmail = new SmtpEmail();
        smtpEmail.adrs = new String[] {adr};
        smtpEmail.content = content;
        smtpEmail.subject = subject;
        smtpEmail.password =  "secret";
        smtpEmail.username = "foo";
        smtpHandler.post(smtpEmail);
        log.info("Send email. Adr: " + adr + ", Subject: " + subject);
        return true;
    } 

}
