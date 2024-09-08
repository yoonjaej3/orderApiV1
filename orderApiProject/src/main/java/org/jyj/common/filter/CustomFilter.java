package org.jyj.common.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.jyj.common.response.LogMessage;
import org.slf4j.MDC;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class CustomFilter implements Filter {
    static String threadId;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper((HttpServletResponse) response);

        try {
            threadId = Thread.currentThread().getName();
            MDC.put("threadId", threadId);

            long requestTime = Instant.now().toEpochMilli();
            chain.doFilter(wrappedRequest, wrappedResponse);
            long responseTime = Instant.now().toEpochMilli();

            LogMessage logMessage = getLogMessage(wrappedRequest, wrappedResponse, requestTime, responseTime);
            log.info(logMessage.toLogString());

        } finally {
            wrappedResponse.copyBodyToResponse();
            MDC.remove("threadId");
        }
    }

    private static LogMessage getLogMessage(ContentCachingRequestWrapper wrappedRequest, ContentCachingResponseWrapper wrappedResponse, long requestTime, long responseTime) throws JsonProcessingException {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

        String requestParams = new String(wrappedRequest.getContentAsByteArray());
        String responseParams = new String(wrappedResponse.getContentAsByteArray());

        ZonedDateTime requestDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(requestTime), ZoneId.systemDefault());
        ZonedDateTime responseDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(responseTime), ZoneId.systemDefault());

        return LogMessage.builder()
                .threadId(threadId)
                .method(wrappedRequest.getMethod())
                .url(wrappedRequest.getRequestURI())
                .userAgent(wrappedRequest.getHeader("User-Agent"))
                .host(wrappedRequest.getHeader("host"))
                .clientIp(wrappedRequest.getRemoteAddr())
                .requestParams(requestParams)
                .responseParams(responseParams)
                .requestAt(requestDateTime.format(dateFormatter))
                .responseAt(responseDateTime.format(dateFormatter))
                .elapsedTimeInMS(responseTime - requestTime)
                .build();

    }


    @Override
    public void destroy() {
        Filter.super.destroy();
    }

}
