package com.productService.security;

import com.common.constants.PublicRoutes;
import com.common.security.JwtService;
import com.common.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final RedisTemplate<String,String> redisTemplate;

    public JwtFilter(JwtService jwtService,RedisTemplate<String,String> redisTemplate) {
        this.jwtService = jwtService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        log.info("Entered into Product service :::::");
        String path = request.getRequestURI();

        for (String route : PublicRoutes.ROUTES) {
            if (path.startsWith(route)) {
                log.info("Entered into public route api::");
                filterChain.doFilter(request, response);
                return;
            }
        }


        String authHeader = request.getHeader("Authorization");

        // No token → just continue (SecurityConfig handles access)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or expired token");
            return;
        }

        String token = authHeader.substring(7);

        // ❗ Let GlobalExceptionHandler handle all failures
        if (!jwtService.validateToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or expired token");
            return;
        }

        String username = jwtService.extractUserName(token);
        String branchCode = jwtService.extractBranchCode(token);
        String jti = jwtService.extractJti(token);
        long expirationTime = jwtService.extractExpiration(token).getTime();

        if (username == null || username.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or userId not present.");
            return;
        }

        if(isBlackListed(jti)){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Your session is already logged off. Please do fresh login.");
            return;
        }


        UserPrincipal principal =
                new UserPrincipal(username, null, branchCode,jti,expirationTime, Collections.emptyList());

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        principal,
                        null,
                        principal.getAuthorities()
                );

        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }

    private boolean isBlackListed(String jti) {

        try {
            Boolean isBlackListed = redisTemplate.hasKey("blacklist:"+jti);
            if(Boolean.TRUE.equals(isBlackListed)){
                return true;
            }
            return false;


        }catch (Exception ex) {

            log.error(
                    "Redis unavailable. Skipping blacklist check.",
                    ex
            );

            // FAIL OPEN
            return false;
        }

    }
}