package com.email.config;

import com.email.util.cache.SystemIdCache;
import com.email.util.filter.RequestValidationFilter;
import com.email.util.helper.HttpServletRequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {
    private final ObjectMapper objectMapper;
    private final SystemIdCache systemIdCache;
    private final HttpServletRequestHandler handler;

    @Bean
    public FilterRegistrationBean<RequestValidationFilter> requestFilter() {
        FilterRegistrationBean<RequestValidationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestValidationFilter(objectMapper,systemIdCache,handler));
        registrationBean.addUrlPatterns("/api/v1/emails/*");
        return  registrationBean;
    }
}
