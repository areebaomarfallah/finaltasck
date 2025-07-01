package com.email.email.controller;

import com.email.email.dto.EmailRequest;
import com.email.email.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/send-email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);

    @PostMapping
    public ResponseEntity<String> sendEmail(@Valid @RequestBody EmailRequest request) {
        emailService.sendEmail(request);
        logger.info("Email sent to {}", request.getEmail());
        return ResponseEntity.ok("Email sent successfully");
    }
}
