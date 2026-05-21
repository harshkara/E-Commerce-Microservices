package com.taskmanager.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.UUID;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

     private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);
     private static final String CORREALTION_ID = "correlationId";

     @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{

         long startTime = System.currentTimeMillis();

         String correlationId = UUID.randomUUID().toString();

         MDC.put(CORREALTION_ID,correlationId);

         response.setHeader("X-Correlation-ID",correlationId);

         ContentCachingResponseWrapper wrappedResponse =
                 new ContentCachingResponseWrapper(response);
         try{
             filterChain.doFilter(request,wrappedResponse);
         }finally {
             byte[] content = wrappedResponse.getContentAsByteArray();
             String responseBody = new String(content, response.getCharacterEncoding());
             long duration = System.currentTimeMillis() - startTime;
             log.info("Method:{} URI={} STATUS={} TIME={}ms BODY={}",request.getMethod(),request.getRequestURI(),response.getStatus(),duration,responseBody);

             wrappedResponse.copyBodyToResponse();

             MDC.clear();
         }
     }
}
