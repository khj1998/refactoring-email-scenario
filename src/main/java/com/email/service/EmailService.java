package com.email.service;

import com.email.common.BaseResponse;
import com.email.dto.request.EmailListSendRequestDto;
import com.email.dto.request.EmailSendRequestDto;
import com.email.dto.response.EmailLogResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EmailService {
    BaseResponse<EmailLogResponseDto> getEmailLogWithId(Long Id);
    BaseResponse<List<EmailLogResponseDto>> getEmailLogWithTransactionId(String transactionId);
    BaseResponse<List<EmailLogResponseDto>> getEmailLogsWithAddress(String address, LocalDateTime startDate,LocalDateTime endDate);
    BaseResponse<List<EmailLogResponseDto>> getEmailLogsWithStatus(String status,LocalDateTime startDate,LocalDateTime endDate);
    BaseResponse sendEmail(EmailSendRequestDto requestDto);
    BaseResponse sendEmailList(EmailListSendRequestDto requestDto);
}
