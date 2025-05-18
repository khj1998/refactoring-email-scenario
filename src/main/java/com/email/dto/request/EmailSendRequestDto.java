package com.email.dto.request;

import com.email.common.StatusCodeEnum;
import com.email.domain.EmailData;
import com.email.exception.BaseException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailSendRequestDto {
    private String emailAddress;
    private String text;
    private LocalDateTime reqDate;
    private String transactionId;

    public EmailData toEmailData() {
        return EmailData.builder()
                .text(getText())
                .emailAddress(emailAddress)
                .reqDate(reqDate)
                .transactionId(transactionId)
                .status("pending")
                .build();
    }

    public void validateEmailFormat() {
        if (this.emailAddress.matches("^[a-zA-Z0-9]@test\\.com$")) {
            throw new BaseException(StatusCodeEnum.INVALID_EMAIL_FORMAT);
        }
    }
}
