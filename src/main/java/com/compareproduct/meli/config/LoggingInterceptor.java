package com.compareproduct.meli.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(LoggingInterceptor.class);
    private static final String REQUEST_ID = "requestId";
    private static final String START_TIME = "startTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestId = UUID.randomUUID().toString().substring(0, 8);
        long startTime = System.currentTimeMillis();
        
        MDC.put(REQUEST_ID, requestId);
        MDC.put(START_TIME, String.valueOf(startTime));
        
        // RED - Rate: Log da requisição recebida
        log.info("REQUEST_RECEIVED method={} uri={} requestId={} userAgent={}", 
                request.getMethod(), 
                request.getRequestURI(), 
                requestId,
                request.getHeader("User-Agent"));
        
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
                               Object handler, Exception ex) {
        try {
            String requestId = MDC.get(REQUEST_ID);
            String startTimeStr = MDC.get(START_TIME);
            
            if (startTimeStr != null) {
                long startTime = Long.parseLong(startTimeStr);
                long duration = System.currentTimeMillis() - startTime;
                
                // RED - Duration: Log da duração da requisição
                // RED - Errors: Log de erros se houver
                if (ex != null) {
                    log.error("REQUEST_ERROR method={} uri={} requestId={} status={} duration={}ms error={}", 
                            request.getMethod(), 
                            request.getRequestURI(), 
                            requestId,
                            response.getStatus(),
                            duration,
                            ex.getMessage());
                } else if (response.getStatus() >= 400) {
                    log.warn("REQUEST_ERROR method={} uri={} requestId={} status={} duration={}ms", 
                            request.getMethod(), 
                            request.getRequestURI(), 
                            requestId,
                            response.getStatus(),
                            duration);
                } else {
                    log.info("REQUEST_COMPLETED method={} uri={} requestId={} status={} duration={}ms", 
                            request.getMethod(), 
                            request.getRequestURI(), 
                            requestId,
                            response.getStatus(),
                            duration);
                }
            }
        } finally {
            MDC.clear();
        }
    }
}