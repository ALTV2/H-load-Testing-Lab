package com.tveritin.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tveritin.config.RequestQueue;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostRequestFilterTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private RequestQueue requestQueue;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private ServletResponse servletResponse;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private PostRequestFilter filter;

    @BeforeEach
    void setUp() {
        filter = new PostRequestFilter(objectMapper, requestQueue);
    }

    @Test
    void testDoFilter_PostRequestWithBody() throws Exception {
        String requestBody = "{\"key\":\"value\"}";
        when(httpServletRequest.getMethod()).thenReturn("POST");
        when(httpServletRequest.getInputStream()).thenReturn(new TestServletInputStream(requestBody));
        when(objectMapper.readValue(requestBody, Object.class)).thenReturn(new Object());

        filter.doFilter(httpServletRequest, servletResponse, filterChain);

        verify(httpServletRequest).getMethod();
        verify(httpServletRequest).getInputStream();
        verify(objectMapper).readValue(requestBody, Object.class);
        verify(requestQueue).add(any(Object.class));
        verify(filterChain).doFilter(any(PostRequestFilter.CachedBodyHttpServletRequest.class), eq(servletResponse));
    }

    @Test
    void testDoFilter_PostRequestWithoutBody() throws Exception {
        when(httpServletRequest.getMethod()).thenReturn("POST");
        when(httpServletRequest.getInputStream()).thenReturn(new TestServletInputStream(""));

        filter.doFilter(httpServletRequest, servletResponse, filterChain);

        verify(httpServletRequest).getMethod();
        verify(httpServletRequest).getInputStream();
        verifyNoInteractions(objectMapper);
        verifyNoInteractions(requestQueue);
        verify(filterChain).doFilter(any(PostRequestFilter.CachedBodyHttpServletRequest.class), eq(servletResponse));
    }

    @Test
    void testDoFilter_NonPostRequest() throws Exception {
        when(httpServletRequest.getMethod()).thenReturn("GET");

        filter.doFilter(httpServletRequest, servletResponse, filterChain);

        verify(httpServletRequest).getMethod();
        verifyNoInteractions(objectMapper, requestQueue);
        verify(filterChain).doFilter(httpServletRequest, servletResponse);
    }

    @Test
    void testCachedBodyHttpServletRequest_CacheBody() throws IOException {
        String requestBody = "test body";
        when(httpServletRequest.getInputStream()).thenReturn(new TestServletInputStream(requestBody));

        PostRequestFilter.CachedBodyHttpServletRequest cachedRequest =
                new PostRequestFilter.CachedBodyHttpServletRequest(httpServletRequest);

        String cachedBody = new String(cachedRequest.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        assertEquals(requestBody, cachedBody, "Кэшированное тело должно совпадать с исходным");

        String rereadBody = new String(cachedRequest.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        assertEquals(requestBody, rereadBody, "Кэшированное тело должно быть доступно для повторного чтения");

        String readerBody = cachedRequest.getReader().lines().collect(java.util.stream.Collectors.joining());
        assertEquals(requestBody, readerBody, "getReader должен возвращать кэшированное тело");
    }

    private static class TestServletInputStream extends ServletInputStream {
        private final ByteArrayInputStream inputStream;

        public TestServletInputStream(String content) {
            this.inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        }

        @Override
        public boolean isFinished() {
            return inputStream.available() == 0;
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
            return inputStream.read();
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            return inputStream.read(b, off, len);
        }
    }
}