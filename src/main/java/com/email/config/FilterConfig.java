package com.email.config;

import com.email.util.filter.RequestValidationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {
    private final ObjectMapper objectMapper;

    @Bean
    public FilterRegistrationBean<RequestValidationFilter> requestFilter() {
        FilterRegistrationBean<RequestValidationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestValidationFilter(objectMapper));
        registrationBean.addUrlPatterns("/api/v1/emails/*");
        return  registrationBean;
    }
}
