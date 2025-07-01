package com.library.library_management_system.client;

import com.library.library_management_system.dto.EmailRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "email-service", url = "http://localhost:8082")
public interface EmailClient {

    @PostMapping("/send-email")
    void sendEmail(@RequestBody EmailRequest request);
}
