package com.email.domain;

import com.email.common.StatusCodeEnum;
import com.email.dto.request.EmailSendRequestDto;
import com.email.exception.BaseException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "email_data")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text",nullable = false,columnDefinition = "varchar(2000)",length = 2000)
    private String text;

    @Column(name = "email_address",nullable = false,columnDefinition = "varchar(64)",length = 64)
    private String emailAddress;

    @Column(name = "transaction_id",nullable = false,columnDefinition = "varchar(64)",length = 64)
    private String transactionId;

    @Column(name = "req_date",nullable = false,columnDefinition = "datetime")
    private LocalDateTime reqDate;

    @Column(name = "sent_date",columnDefinition = "datetime")
    private LocalDateTime sentDate;

    @Column(name = "status",nullable = false,columnDefinition = "varchar(16)",length = 16)
    private String status;

    public static EmailData from(EmailSendRequestDto dto,String transactionId) {
        return EmailData.builder()
                .text(dto.getText())
                .emailAddress(dto.getEmailAddress())
                .reqDate(dto.getReqDate())
                .transactionId(transactionId)
                .status("pending")
                .build();
    }
}
