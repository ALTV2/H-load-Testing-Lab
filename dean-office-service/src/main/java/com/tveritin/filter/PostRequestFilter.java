package com.tveritin.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tveritin.config.RequestQueue;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class PostRequestFilter implements Filter {

    private final ObjectMapper objectMapper;
    private final RequestQueue requestQueue;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        if ("POST".equalsIgnoreCase(httpRequest.getMethod())) {
            // Оборачиваем запрос для кэширования тела
            CachedBodyHttpServletRequest cachedRequest = new CachedBodyHttpServletRequest(httpRequest);

            // Читаем тело запроса из обертки
            StringBuilder requestBody = new StringBuilder();
            try (BufferedReader reader = cachedRequest.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    requestBody.append(line);
                }
            }

            if (!requestBody.isEmpty()) {
                Object parsedObject = objectMapper.readValue(requestBody.toString(), Object.class);
                requestQueue.add(parsedObject);
            }

            // Передаем обернутый запрос дальше по цепочке
            chain.doFilter(cachedRequest, response);
        } else {
            // Для всех остальных методов передаем оригинальный запрос
            chain.doFilter(request, response);
        }
    }

    // Внутренний класс для кэширования тела запроса
    private static class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {
        private final byte[] cachedBody;

        public CachedBodyHttpServletRequest(HttpServletRequest request) throws IOException {
            super(request);
            InputStream requestInputStream = request.getInputStream();
            this.cachedBody = requestInputStream.readAllBytes();
        }

        @Override
        public ServletInputStream getInputStream() {
            return new CachedBodyServletInputStream(cachedBody);
        }

        @Override
        public BufferedReader getReader() {
            return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(cachedBody), StandardCharsets.UTF_8));
        }
    }

    // Внутренний класс для реализации ServletInputStream
    private static class CachedBodyServletInputStream extends ServletInputStream {
        private final ByteArrayInputStream cachedBodyInputStream;

        public CachedBodyServletInputStream(byte[] cachedBody) {
            this.cachedBodyInputStream = new ByteArrayInputStream(cachedBody);
        }

        @Override
        public boolean isFinished() {
            return cachedBodyInputStream.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
            throw new UnsupportedOperationException("Not implemented");
        }

        @Override
        public int read() throws IOException {
            return cachedBodyInputStream.read();
        }
    }
}