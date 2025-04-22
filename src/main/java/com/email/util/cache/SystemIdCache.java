package com.email.util.cache;

import com.email.domain.RequestSystem;
import com.email.repository.RequestSystemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
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

    @EventListener(ContextRefreshedEvent.class)
    public void initializeCache() {
        log.info("Application Context has been ready for initializing request system Ids");

        List<RequestSystem> requestSystemList = requestSystemRepository.findAll();
        for (RequestSystem requestSystem : requestSystemList) {
            systemIdsCache.put(requestSystem.getSystemId(),requestSystem);
        }

        log.info("Application Context has fetched request system ids successfully");
    }
}
