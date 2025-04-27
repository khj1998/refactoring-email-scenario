package com.email.util.cache;

import com.email.common.StatusCodeEnum;
import com.email.domain.RequestSystem;
import com.email.exception.BaseException;
import com.email.repository.RequestSystemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SystemIdCache {
    private final RequestSystemRepository requestSystemRepository;
    private final ConcurrentHashMap<String, RequestSystem> systemIdsCache;

    public SystemIdCache(RequestSystemRepository requestSystemRepository) {
        systemIdsCache = new ConcurrentHashMap<>();
        this.requestSystemRepository = requestSystemRepository;
    }

    @Transactional(readOnly = true)
    @EventListener(ContextRefreshedEvent.class)
    public void initializeCache() {
        log.info("Application Context has been ready for initializing request system Ids");

        List<RequestSystem> requestSystemList = requestSystemRepository.findAll();
        for (RequestSystem requestSystem : requestSystemList) {
            systemIdsCache.put(requestSystem.getSystemId(), requestSystem);
        }

        log.info("Application Context has fetched request system ids successfully");
    }

    @Transactional
    public void checkServiceIdExistsFromCache(String serviceId) {
        log.info("Try to find request service id from cache");

        if (!systemIdsCache.containsKey(serviceId)) {
            RequestSystem requestSystem = requestSystemRepository.findBySystemId(serviceId)
                    .orElseThrow(() -> new BaseException(StatusCodeEnum.SYSTEM_NOT_EXISTS));
            systemIdsCache.put(serviceId,requestSystem);
        }

        log.info("Finding request service id from cache successfully");
    }
}
