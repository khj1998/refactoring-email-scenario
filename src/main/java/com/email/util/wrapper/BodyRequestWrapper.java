package com.email.util.wrapper;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class BodyRequestWrapper extends HttpServletRequestWrapper {
    private final String modifiedBody;

    public BodyRequestWrapper(HttpServletRequest request, String modifiedBody) {
        super(request);
        this.modifiedBody = modifiedBody;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        byte[] modifiedBodyBytes = modifiedBody.getBytes(StandardCharsets.UTF_8);

        return new ServletInputStream() {
            private final ByteArrayInputStream stream = new ByteArrayInputStream(modifiedBodyBytes);

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return stream.read();
            }
        };
    }
}
