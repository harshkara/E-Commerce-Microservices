package com.practice.taskmanager.config;

import com.practice.taskmanager.exception.JwtAuthenticationException;
import com.practice.taskmanager.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new JwtAuthenticationException("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);

        String username;
        String branchCode;

        try {
            username = jwtService.extractUserName(token);
            branchCode = jwtService.extractBranchCode(token);
        } catch (Exception e) {
            // Any error parsing the token → treat as tampered/invalid
            throw new JwtAuthenticationException("Invalid JWT token: " + e.getMessage());
        }

        if (username == null || username.isEmpty()) {
            throw new JwtAuthenticationException("Token does not contain a username");
        }

        if (!jwtService.validateToken(token)) {
            throw new JwtAuthenticationException("Token is expired or invalid");
        }

        // Set authentication
        UserPrincipal principal = new UserPrincipal(username, branchCode, Collections.emptyList());
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
