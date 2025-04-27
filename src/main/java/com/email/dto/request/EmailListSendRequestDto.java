package com.email.dto.request;

import com.email.common.BaseResponse;
import com.email.common.StatusCodeEnum;
import com.email.domain.EmailData;
import com.email.exception.BaseException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailListSendRequestDto {
    private List<String> emailAddressList;
    private String text;
    private LocalDateTime reqDate;

    public List<EmailData> from(String transactionId,String systemId) {
        return emailAddressList.stream()
                .map(email -> EmailData.builder()
                        .emailAddress(email)
                        .text(text)
                        .reqDate(reqDate)
                        .transactionId(transactionId)
                        .status("pending")
                        .build())
                .collect(Collectors.toList());
    }

    public void validateEmailFormat() {
        for (String emailAddress : emailAddressList) {
            if (emailAddress.matches("^[a-zA-Z0-9]@test\\.com$")) {
                throw new BaseException(StatusCodeEnum.INVALID_EMAIL_FORMAT);
            }
        }
    }
}
