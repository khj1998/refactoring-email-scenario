package com.email.dto.response;

import com.email.domain.EmailData;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailLogResponseDto {
    private Long id;
    private String text;
    private String status;
    private String transactionId;
    private LocalDateTime reqDate;
    private LocalDateTime sentDate;

    public static EmailLogResponseDto of(EmailData emailData) {
        return EmailLogResponseDto.builder()
                .id(emailData.getId())
                .text(emailData.getText())
                .status(emailData.getStatus())
                .transactionId(emailData.getTransactionId())
                .reqDate(emailData.getReqDate())
                .sentDate(emailData.getSentDate())
                .build();
    }

    public static List<EmailLogResponseDto> of(List<EmailData> emailDataList) {
        return emailDataList.stream()
                .map(data -> EmailLogResponseDto.builder()
                        .id(data.getId())
                        .text(data.getText())
                        .status(data.getStatus())
                        .transactionId(data.getTransactionId())
                        .reqDate(data.getReqDate())
                        .sentDate(data.getSentDate())
                        .build())
                .collect(Collectors.toList());
    }
}
