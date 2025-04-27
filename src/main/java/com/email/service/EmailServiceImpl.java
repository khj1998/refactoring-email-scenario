package com.email.service;

import com.email.common.BaseResponse;
import com.email.common.StatusCodeEnum;
import com.email.domain.EmailData;
import com.email.dto.request.EmailListSendRequestDto;
import com.email.dto.request.EmailSendRequestDto;
import com.email.dto.response.EmailLogResponseDto;
import com.email.exception.BaseException;
import com.email.repository.EmailRepository;
import com.email.repository.RequestSystemRepository;
import com.email.util.TokenUtils;
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
    private final RequestSystemRepository requestSystemRepository;

    @Override
    @Transactional(readOnly = true)
    public BaseResponse<EmailLogResponseDto> getEmailLogWithId(String token,Long id) {
        String decodedToken = TokenUtils.decodeToken(token);
        String[] decodedTokenArray = TokenUtils.parseToken(decodedToken);

        TokenUtils.validateTokenArrayLength(decodedTokenArray);
        TokenUtils.validateRequestMillisTime(decodedTokenArray);

        requestSystemRepository.findBySystemId(TokenUtils.getRequestSystemId(decodedTokenArray))
                .orElseThrow(() -> new BaseException(StatusCodeEnum.SYSTEM_NOT_EXISTS));

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
    public BaseResponse<List<EmailLogResponseDto>> getEmailLogWithTransactionId(String token) {
        String decodedToken = TokenUtils.decodeToken(token);
        String[] decodedTokenArray = TokenUtils.parseToken(decodedToken);

        TokenUtils.validateTokenArrayLength(decodedTokenArray);
        TokenUtils.validateRequestMillisTime(decodedTokenArray);

        requestSystemRepository.findBySystemId(TokenUtils.getRequestSystemId(decodedTokenArray))
                .orElseThrow(() -> new BaseException(StatusCodeEnum.SYSTEM_NOT_EXISTS));

        String transactionId = TokenUtils.getTransactionId(decodedTokenArray);
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
    public BaseResponse<List<EmailLogResponseDto>> getEmailLogsWithAddress(String token,String address, LocalDateTime startDate, LocalDateTime endDate) {
        String decodedToken = TokenUtils.decodeToken(token);
        String[] decodedTokenArray = TokenUtils.parseToken(decodedToken);

        TokenUtils.validateTokenArrayLength(decodedTokenArray);
        TokenUtils.validateRequestMillisTime(decodedTokenArray);

        requestSystemRepository.findBySystemId(TokenUtils.getRequestSystemId(decodedTokenArray))
                .orElseThrow(() -> new BaseException(StatusCodeEnum.SYSTEM_NOT_EXISTS));

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
    public BaseResponse<List<EmailLogResponseDto>> getEmailLogsWithStatus(String token, String status, LocalDateTime startDate, LocalDateTime endDate) {
        String decodedToken = TokenUtils.decodeToken(token);
        String[] decodedTokenArray = TokenUtils.parseToken(decodedToken);

        TokenUtils.validateTokenArrayLength(decodedTokenArray);
        TokenUtils.validateRequestMillisTime(decodedTokenArray);

        requestSystemRepository.findBySystemId(TokenUtils.getRequestSystemId(decodedTokenArray))
                .orElseThrow(() -> new BaseException(StatusCodeEnum.SYSTEM_NOT_EXISTS));

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
    public BaseResponse sendEmail(String token,EmailSendRequestDto requestDto) {
        requestDto.validateEmailFormat();

        String tokenArray = TokenUtils.decodeToken(token);
        String[] decodedTokenArray = TokenUtils.parseToken(tokenArray);

        TokenUtils.validateTokenArrayLength(decodedTokenArray);
        TokenUtils.validateRequestMillisTime(decodedTokenArray);

        requestSystemRepository.findBySystemId(TokenUtils.getRequestSystemId(decodedTokenArray))
                .orElseThrow(() -> new BaseException(StatusCodeEnum.SYSTEM_NOT_EXISTS));

        String transactionId = TokenUtils.getTransactionId(decodedTokenArray);

        EmailData emailData = requestDto.from(transactionId);
        emailRepository.save(emailData);

        return BaseResponse.builder()
                .statusCode(StatusCodeEnum.EMAIL_SEND_SUCCESS.getStatusCode())
                .message(StatusCodeEnum.EMAIL_SEND_SUCCESS.getMessage())
                .build();
    }

    @Override
    @Transactional
    public BaseResponse sendEmailList(String token,EmailListSendRequestDto requestDto) {
        String tokenArray = TokenUtils.decodeToken(token);
        String[] decodedTokenArray = TokenUtils.parseToken(tokenArray);

        TokenUtils.validateTokenArrayLength(decodedTokenArray);
        TokenUtils.validateRequestMillisTime(decodedTokenArray);

        requestSystemRepository.findBySystemId(TokenUtils.getRequestSystemId(decodedTokenArray))
                .orElseThrow(() -> new BaseException(StatusCodeEnum.SYSTEM_NOT_EXISTS));

        String transactionId = TokenUtils.getTransactionId(decodedTokenArray);
        String systemId = TokenUtils.getRequestSystemId(decodedTokenArray);

        List<EmailData> emailDataList = requestDto.from(transactionId,systemId);
        emailRepository.saveAll(emailDataList);

        return BaseResponse.builder()
                .statusCode(StatusCodeEnum.EMAIL_SEND_SUCCESS.getStatusCode())
                .message(StatusCodeEnum.EMAIL_SEND_SUCCESS.getMessage())
                .build();
    }
}
