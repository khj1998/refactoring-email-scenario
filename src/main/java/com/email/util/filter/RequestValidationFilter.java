package com.email.util.filter;

import com.email.common.BaseResponse;
import com.email.common.StatusCodeEnum;
import com.email.exception.BaseException;
import com.email.service.RequestSystemService;
import com.email.util.TokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class RequestValidationFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;
    private final RequestSystemService requestSystemService;

    public RequestValidationFilter(ObjectMapper objectMapper,RequestSystemService requestSystemService) {
        this.objectMapper = objectMapper;
        this.requestSystemService = requestSystemService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
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
            requestSystemService.validateSystemIdExists(serviceId);
        } catch (BaseException ex) {
            createBaseExceptionResponse(res,ex);
            return;
        }

        log.info("Request validation filter has been ended successfully with request URI : {}",((HttpServletRequest) req).getRequestURI());
        filterChain.doFilter(req,res);
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
