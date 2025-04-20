package com.examplejjwt.jwtauth.controller;

import com.examplejjwt.jwtauth.dto.MailRequest;
import com.examplejjwt.jwtauth.service.EmailService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
public class EmailController {
    private final EmailService service;

    public EmailController(EmailService service) {
        this.service = service;
    }

    @PostMapping
    public String sentmailtouser(@RequestBody MailRequest request){
        service.sendEmail(request.getTo(), request.getSubject(), request.getBody());
        return "Message Sent Successfully";
    }
}
