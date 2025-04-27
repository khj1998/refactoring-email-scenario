package com.email.repository;

import com.email.domain.EmailData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EmailRepository extends JpaRepository<EmailData,Long> {
    List<EmailData> findByTransactionId(String transactionId);

    @Query(value = "select e from EmailData e where e.emailAddress =:address and (e.reqDate between :startDate and :endDate)")
    List<EmailData> findEmailsByAddress(@Param("address")String address,@Param("startDate")LocalDateTime startDate,@Param("endDate")LocalDateTime endDate);
}
