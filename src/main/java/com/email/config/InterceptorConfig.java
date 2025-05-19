package com.email.config;

import com.email.util.cache.SystemIdCache;
import com.email.util.helper.HttpServletRequestHandler;
import com.email.util.interceptor.RequestValidationInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {
    private final ObjectMapper objectMapper;
    private final SystemIdCache systemIdCache;
    private final HttpServletRequestHandler handler;

    @Bean
    public RequestValidationInterceptor requestValidationInterceptor() {
        return new RequestValidationInterceptor(objectMapper, systemIdCache, handler);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestValidationInterceptor())
                .addPathPatterns("/**");
    }
}
