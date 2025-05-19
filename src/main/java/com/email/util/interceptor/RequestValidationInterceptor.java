package com.email.util.interceptor;

import com.email.common.BaseResponse;
import com.email.common.StatusCodeEnum;
import com.email.exception.BaseException;
import com.email.util.TokenUtils;
import com.email.util.cache.SystemIdCache;
import com.email.util.helper.HttpServletRequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Slf4j
public class RequestValidationInterceptor implements HandlerInterceptor {
    private final ObjectMapper objectMapper;
    private final SystemIdCache systemIdCache;
    private final HttpServletRequestHandler handler;

    public RequestValidationInterceptor(ObjectMapper objectMapper, SystemIdCache systemIdCache, HttpServletRequestHandler handler) {
        this.objectMapper = objectMapper;
        this.systemIdCache = systemIdCache;
        this.handler = handler;
    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        log.info("Request validation filter has been started with request URI : {}" ,req.getRequestURI());
        String token = req.getHeader("token");

        try {
            if (token == null || token.isBlank()) {
                throw new BaseException(StatusCodeEnum.INVALID_TOKEN_HEADER_REQUEST);
            }

            String decodedToken = TokenUtils.decodeToken(token);
            String[] decodedTokenArray = TokenUtils.parseToken(decodedToken);
            TokenUtils.validateTokenArrayLength(decodedTokenArray);
            TokenUtils.validateRequestMillisTime(decodedTokenArray);

            String serviceId = TokenUtils.getRequestSystemId(decodedTokenArray);
            systemIdCache.checkServiceIdExistsFromCache(serviceId);

            if (req.getMethod().equalsIgnoreCase("POST")) {
                String transactionId = TokenUtils.getTransactionId(decodedTokenArray);
                req = this.handler.handleHttpServletRequest(req, transactionId);
            }

            log.info("Request validation interceptor ended successfully with request URI: {}", req.getRequestURI());
            return true;
        } catch (BaseException ex) {
            createBaseExceptionResponse(res, ex);
            return false;
        }
    }

    private void createBaseExceptionResponse(HttpServletResponse res, BaseException ex) throws IOException {
        res.setStatus(HttpServletResponse.SC_OK);
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        BaseResponse baseResponse = BaseResponse.builder()
                .statusCode(ex.getStatusCode())
                .message(ex.getMessage())
                .build();

        String jsonResponse = objectMapper.writeValueAsString(baseResponse);
        res.getWriter().write(jsonResponse);
    }
}
