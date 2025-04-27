package com.email.service;

import com.email.common.BaseResponse;
import com.email.dto.request.EmailListSendRequestDto;
import com.email.dto.request.EmailSendRequestDto;
import com.email.dto.response.EmailLogResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EmailService {
    BaseResponse<EmailLogResponseDto> getEmailLogWithId(String token,Long Id);
    BaseResponse<List<EmailLogResponseDto>> getEmailLogWithTransactionId(String token);
    BaseResponse<List<EmailLogResponseDto>> getEmailLogsWithAddress(String token,String address, LocalDateTime startDate,LocalDateTime endDate);
    BaseResponse<List<EmailLogResponseDto>> getEmailLogsWithStatus(String token,String status,LocalDateTime startDate,LocalDateTime endDate);
    BaseResponse sendEmail(String token,EmailSendRequestDto requestDto);
    BaseResponse sendEmailList(String token,EmailListSendRequestDto requestDto);
}
