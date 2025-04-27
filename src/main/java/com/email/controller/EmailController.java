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
    public ResponseEntity<BaseResponse<List<EmailLogResponseDto>>> getEmail(@RequestHeader("token") String token,@RequestParam String emailAddress,
                                                                            @RequestParam LocalDateTime startDate,@RequestParam LocalDateTime endDate) {
        return ResponseEntity.ok(emailService.getEmailLogsWithAddress(token,emailAddress,startDate,endDate));
    }

    @GetMapping("/id")
    public ResponseEntity<BaseResponse<EmailLogResponseDto>> getEmailWithId(@RequestHeader("token") String token,@RequestParam Long id) {
        return ResponseEntity.ok(emailService.getEmailLogWithId(token,id));
    }

    @GetMapping("/transactionId")
    public ResponseEntity<BaseResponse<List<EmailLogResponseDto>>> getEmailWithTransactionId(@RequestHeader("token") String token) {
        return ResponseEntity.ok(emailService.getEmailLogWithTransactionId(token));
    }

    @GetMapping("/status")
    public ResponseEntity<BaseResponse<List<EmailLogResponseDto>>> getEmailWithStatus(@RequestHeader("token") String token,@RequestParam String status,
                                                                                      @RequestParam LocalDateTime startDate,@RequestParam LocalDateTime endDate) {
        return ResponseEntity.ok(emailService.getEmailLogsWithStatus(token,status,startDate,endDate));
    }

    @PostMapping
    public ResponseEntity<BaseResponse> sendEmail(@RequestHeader("token") String token,@RequestBody EmailSendRequestDto requestDto) {
        return ResponseEntity.ok(emailService.sendEmail(token,requestDto));
    }

    @PostMapping("/list")
    public ResponseEntity<BaseResponse> sendEmail(@RequestHeader("token") String token,@RequestBody EmailListSendRequestDto requestDto) {
        return ResponseEntity.ok(emailService.sendEmailList(token,requestDto));
    }
}
