package com.email.service;

import com.email.common.BaseResponse;
import com.email.common.StatusCodeEnum;
import com.email.domain.EmailData;
import com.email.domain.RequestSystem;
import com.email.dto.request.EmailSendRequestDto;
import com.email.dto.response.EmailLogResponseDto;
import com.email.repository.EmailRepository;
import com.email.repository.RequestSystemRepository;
import com.email.util.TokenUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.yml")
class EmailServiceImplTest {

    @Mock
    private RequestSystemRepository requestSystemRepository;

    @Mock
    private EmailRepository emailRepository;

    @InjectMocks
    private EmailServiceImpl emailService;

    private String testToken;

    @BeforeEach
    void init() {
        String transactionId = UUID.randomUUID().toString();
        String serviceId = UUID.randomUUID().toString();
        String timestamp = String.valueOf(System.currentTimeMillis());
        String rawToken = transactionId + ":" + serviceId + ":" + timestamp;

        testToken = Base64.getEncoder().encodeToString(rawToken.getBytes());
    }

    @Test
    @DisplayName("이메일 로그 식별자(pk)로 이력을 조회하는 테스트")
    void getEmailLogWithId() {
        EmailSendRequestDto requestDto = new EmailSendRequestDto("test email","test text",LocalDateTime.now());

        String decodedToken = TokenUtils.decodeToken(testToken);
        String[] tokenArray = TokenUtils.parseToken(decodedToken);
        String testTransactionId = TokenUtils.getTransactionId(tokenArray);
        String testSystemId = TokenUtils.getRequestSystemId(tokenArray);

        EmailData emailData = EmailData.builder()
                .id(1L)
                .text("test text")
                .emailAddress("test@test.com")
                .reqDate(requestDto.getReqDate())
                .transactionId(testTransactionId)
                .status("pending")
                .build();

        RequestSystem requestSystem = RequestSystem.builder()
                .id(1L)
                .systemId(testSystemId)
                .systemName("test system")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        when(requestSystemRepository.findBySystemId(anyString()))
                .thenReturn(Optional.of(requestSystem));

        when(emailRepository.findById(anyLong()))
                .thenReturn(Optional.of(emailData));

        BaseResponse<EmailLogResponseDto> responseDto = emailService.getEmailLogWithId(testToken,1L);

        assertEquals(StatusCodeEnum.EMAIL_QUERY_SUCCESS.getStatusCode(),responseDto.getStatusCode());
        assertEquals(StatusCodeEnum.EMAIL_QUERY_SUCCESS.getMessage(),responseDto.getMessage());
    }
}