package com.email.service;

import com.email.common.BaseResponse;
import com.email.common.StatusCodeEnum;
import com.email.domain.EmailData;
import com.email.dto.request.EmailListSendRequestDto;
import com.email.dto.request.EmailSendRequestDto;
import com.email.dto.response.EmailLogResponseDto;
import com.email.exception.BaseException;
import com.email.repository.EmailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final EmailRepository emailRepository;

    @Override
    @Transactional(readOnly = true)
    public BaseResponse<EmailLogResponseDto> getEmailLogWithId(Long id) {
        EmailData emailData = emailRepository.findById(id)
                .orElseThrow(() -> new BaseException(StatusCodeEnum.EMAIL_NOT_EXISTS));

        EmailLogResponseDto responseDto = EmailLogResponseDto.of(emailData);

        return BaseResponse.<EmailLogResponseDto>builder()
                .statusCode(StatusCodeEnum.EMAIL_QUERY_SUCCESS.getStatusCode())
                .message(StatusCodeEnum.EMAIL_QUERY_SUCCESS.getMessage())
                .data(responseDto)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public BaseResponse<List<EmailLogResponseDto>> getEmailLogWithTransactionId(String transactionId) {
        List<EmailData> emailData = emailRepository.findByTransactionId(transactionId);

        List<EmailLogResponseDto> responseDto = EmailLogResponseDto.of(emailData);

        return BaseResponse.<List<EmailLogResponseDto>>builder()
                .statusCode(StatusCodeEnum.EMAIL_QUERY_SUCCESS.getStatusCode())
                .message(StatusCodeEnum.EMAIL_QUERY_SUCCESS.getMessage())
                .data(responseDto)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public BaseResponse<List<EmailLogResponseDto>> getEmailLogsWithAddress(String address, LocalDateTime startDate, LocalDateTime endDate) {
        List<EmailData> emailDataList = emailRepository.findEmailsByAddress(address,startDate,endDate);

        List<EmailLogResponseDto> responseDto = EmailLogResponseDto.of(emailDataList);

        return BaseResponse.<List<EmailLogResponseDto>>builder()
                .statusCode(StatusCodeEnum.EMAIL_QUERY_SUCCESS.getStatusCode())
                .message(StatusCodeEnum.EMAIL_QUERY_SUCCESS.getMessage())
                .data(responseDto)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public BaseResponse<List<EmailLogResponseDto>> getEmailLogsWithStatus(String status, LocalDateTime startDate, LocalDateTime endDate) {
        List<EmailData> emailDataList = emailRepository.findEmailsByStatus(status,startDate,endDate);

        List<EmailLogResponseDto> responseDto = EmailLogResponseDto.of(emailDataList);

        return BaseResponse.<List<EmailLogResponseDto>>builder()
                .statusCode(StatusCodeEnum.EMAIL_QUERY_SUCCESS.getStatusCode())
                .message(StatusCodeEnum.EMAIL_QUERY_SUCCESS.getMessage())
                .data(responseDto)
                .build();
    }

    @Override
    @Transactional
    public BaseResponse sendEmail(EmailSendRequestDto requestDto) {
        requestDto.validateEmailFormat();

        EmailData emailData = requestDto.toEmailData();
        emailRepository.save(emailData);

        return BaseResponse.builder()
                .statusCode(StatusCodeEnum.EMAIL_SEND_SUCCESS.getStatusCode())
                .message(StatusCodeEnum.EMAIL_SEND_SUCCESS.getMessage())
                .build();
    }

    @Override
    @Transactional
    public BaseResponse sendEmailList(EmailListSendRequestDto requestDto) {
        requestDto.validateEmailFormat();

        List<EmailData> emailDataList = requestDto.toEmailDataList();
        emailRepository.saveAll(emailDataList);

        return BaseResponse.builder()
                .statusCode(StatusCodeEnum.EMAIL_SEND_SUCCESS.getStatusCode())
                .message(StatusCodeEnum.EMAIL_SEND_SUCCESS.getMessage())
                .build();
    }
}
