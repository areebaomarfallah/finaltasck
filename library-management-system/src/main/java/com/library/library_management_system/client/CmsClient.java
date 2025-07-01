package com.library.library_management_system.client;

import com.library.library_management_system.dto.TransactionRequestDTO;
import com.library.library_management_system.dto.TransactionResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "cms-service", url = "http://localhost:8083")
public interface CmsClient {
    @PostMapping("/api/transactions/process")
    TransactionResponseDTO processTransaction(@RequestBody TransactionRequestDTO request);
}