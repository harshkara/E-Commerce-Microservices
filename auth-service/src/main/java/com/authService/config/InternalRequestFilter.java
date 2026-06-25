package com.authService.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class InternalRequestFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String internalHeader = request.getHeader("X-Gateway-Service");

        if (!"gateway-service".equals(internalHeader)) {

            response.setStatus(403);

            response.getWriter().write("Direct access denied");

            return;
        }

        filterChain.doFilter(request, response);
    }
}