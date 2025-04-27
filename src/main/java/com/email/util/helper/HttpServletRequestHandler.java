package com.email.util.helper;

import com.email.util.wrapper.BodyRequestWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class HttpServletRequestHandler {
    private final ObjectMapper objectMapper;

    public HttpServletRequest handleHttpServletRequest(HttpServletRequest originRequestServlet,String transactionId) throws IOException {
        String body = modifyBody(getRequestBody(originRequestServlet),transactionId);
        return new BodyRequestWrapper(originRequestServlet,body);
    }

    private String getRequestBody(HttpServletRequest req) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = req.getReader();

        String line;
        while ((line = bufferedReader.readLine())!=null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

    private String modifyBody(String originalBody,String transactionId) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(originalBody);
        ((ObjectNode) jsonNode).put("transactionId", transactionId);

        return objectMapper.writeValueAsString(jsonNode);
    }
}
