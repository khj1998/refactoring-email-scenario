package com.email.service;

import com.email.common.StatusCodeEnum;
import com.email.exception.BaseException;
import com.email.repository.RequestSystemRepository;
import com.email.util.TokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestSystemServiceImpl implements RequestSystemService {
    private final RequestSystemRepository requestSystemRepository;

    @Override
    @Transactional(readOnly = true)
    public void validateSystemIdExists(String systemId) {
        requestSystemRepository.findBySystemId(systemId)
                .orElseThrow(() -> new BaseException(StatusCodeEnum.SYSTEM_NOT_EXISTS));
    }
}
