package com.email.repository;

import com.email.domain.RequestSystem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RequestSystemRepository extends JpaRepository<RequestSystem,Long> {
    Optional<RequestSystem> findBySystemId(String systemId);
}
