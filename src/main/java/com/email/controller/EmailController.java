package com.email.controller;

import com.email.common.BaseResponse;
import com.email.dto.request.EmailListSendRequestDto;
import com.email.dto.request.EmailSendRequestDto;
import com.email.dto.response.EmailLogResponseDto;
import com.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/emails")
public class EmailController {
    private final EmailService emailService;

    @GetMapping
    public ResponseEntity<BaseResponse<List<EmailLogResponseDto>>> getEmail(@RequestParam String emailAddress,
                                                                            @RequestParam LocalDateTime startDate,@RequestParam LocalDateTime endDate) {
        return ResponseEntity.ok(emailService.getEmailLogsWithAddress(emailAddress,startDate,endDate));
    }

    @GetMapping("/id")
    public ResponseEntity<BaseResponse<EmailLogResponseDto>> getEmailWithId(@RequestParam Long id) {
        return ResponseEntity.ok(emailService.getEmailLogWithId(id));
    }

    @GetMapping("/transactionId")
    public ResponseEntity<BaseResponse<List<EmailLogResponseDto>>> getEmailWithTransactionId(@RequestParam String transactionId) {
        return ResponseEntity.ok(emailService.getEmailLogWithTransactionId(transactionId));
    }

    @GetMapping("/status")
    public ResponseEntity<BaseResponse<List<EmailLogResponseDto>>> getEmailWithStatus(@RequestParam String status,@RequestParam LocalDateTime startDate,@RequestParam LocalDateTime endDate) {
        return ResponseEntity.ok(emailService.getEmailLogsWithStatus(status,startDate,endDate));
    }

    @PostMapping
    public ResponseEntity<BaseResponse> sendEmail(@RequestBody EmailSendRequestDto requestDto) {
        return ResponseEntity.ok(emailService.sendEmail(requestDto));
    }

    @PostMapping("/list")
    public ResponseEntity<BaseResponse> sendEmail(@RequestBody EmailListSendRequestDto requestDto) {
        return ResponseEntity.ok(emailService.sendEmailList(requestDto));
    }
}
