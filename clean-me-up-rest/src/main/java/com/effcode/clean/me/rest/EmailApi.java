package com.effcode.clean.me.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EmailApi {

    @Autowired
    EmailHandler emailHandler;

    @RequestMapping("/")
    public ResponseEntity<Void> send(@RequestParam String adr, @RequestParam String subject,
            @RequestParam String content) {
        boolean state = emailHandler.send(adr, subject, content);
        if (state) {
            return new ResponseEntity<Void>(HttpStatus.OK);
        } else {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }
    }

}
