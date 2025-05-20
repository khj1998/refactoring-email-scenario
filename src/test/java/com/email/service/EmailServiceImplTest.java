package com.email.service;

import com.email.common.BaseResponse;
import com.email.common.StatusCodeEnum;
import com.email.domain.EmailData;
import com.email.dto.request.EmailSendRequestDto;
import com.email.dto.response.EmailLogResponseDto;
import com.email.repository.EmailRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.yml")
class EmailServiceImplTest {

    @Mock
    private EmailRepository emailRepository;

    @InjectMocks
    private EmailServiceImpl emailService;

    @Test
    @DisplayName("이메일 로그 식별자로 이력을 조회하는 테스트")
    void getEmailLogWithId() {
        String testTransactionId = UUID.randomUUID().toString();

        EmailSendRequestDto requestDto = new EmailSendRequestDto("test email","test text",LocalDateTime.now(),testTransactionId);

        EmailData emailData = EmailData.builder()
                .id(1L)
                .text("test text")
                .emailAddress("test@test.com")
                .reqDate(requestDto.getReqDate())
                .transactionId(testTransactionId)
                .status("pending")
                .build();

        when(emailRepository.findById(anyLong()))
                .thenReturn(Optional.of(emailData));

        BaseResponse<EmailLogResponseDto> responseDto = emailService.getEmailLogWithId(1L);

        assertEquals(StatusCodeEnum.EMAIL_QUERY_SUCCESS.getStatusCode(),responseDto.getStatusCode());
        assertEquals(StatusCodeEnum.EMAIL_QUERY_SUCCESS.getMessage(),responseDto.getMessage());
    }

    @Test
    @DisplayName("이메일 발송 데이터 save 테스트")
    void sendEmail() {
        String testTransactionId = UUID.randomUUID().toString();

        EmailSendRequestDto requestDto = new EmailSendRequestDto("test@test.com","test text",LocalDateTime.now(),testTransactionId);

        BaseResponse responseDto = emailService.sendEmail(requestDto);

        assertEquals(StatusCodeEnum.EMAIL_SEND_SUCCESS.getStatusCode(),responseDto.getStatusCode());
        assertEquals(StatusCodeEnum.EMAIL_SEND_SUCCESS.getMessage(),responseDto.getMessage());
    }

    @Test
    @DisplayName("트랜잭션 Id로 이메일 발송 로그 조회")
    void getEmailLogWithTransactionId() {
        String testTransactionId = UUID.randomUUID().toString();

        EmailData emailData = EmailData.builder()
                .id(1L)
                .text("test text")
                .emailAddress("test@test.com")
                .reqDate(LocalDateTime.now())
                .transactionId(testTransactionId)
                .status("pending")
                .build();

        when(emailRepository.findByTransactionId(testTransactionId))
                .thenReturn(List.of(emailData));

        BaseResponse responseDto = emailService.getEmailLogWithTransactionId(testTransactionId);

        assertEquals(StatusCodeEnum.EMAIL_QUERY_SUCCESS.getStatusCode(),responseDto.getStatusCode());
        assertEquals(StatusCodeEnum.EMAIL_QUERY_SUCCESS.getMessage(),responseDto.getMessage());
    }
}