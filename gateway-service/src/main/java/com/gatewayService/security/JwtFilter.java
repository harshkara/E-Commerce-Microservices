package com.gatewayService.security;

import com.common.security.JwtService;
import com.common.constants.PublicRoutes;
import com.common.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Slf4j
@Component
public class JwtFilter implements WebFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;

    private final ReactiveRedisTemplate<String, String> redisTemplate;

    public JwtFilter(JwtService jwtService, ReactiveRedisTemplate<String, String> redisTemplate) {
        this.jwtService = jwtService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        log.info("Entered into jwt filter::");
        String path = exchange.getRequest().getURI().getPath();

        for (String route : PublicRoutes.ROUTES) {
            if (path.startsWith(route)) {
                log.info("Entered into public route api::");
                return chain.filter(exchange);
            }
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            return unauthorized(exchange);
        }

        String token = authHeader.substring(BEARER_PREFIX.length());

        if (!jwtService.validateToken(token)) {
            return unauthorized(exchange);
        }

        String username = jwtService.extractUserName(token);
        String branchCode = jwtService.extractBranchCode(token);
        String jti = jwtService.extractJti(token);
        long expirationTime = jwtService.extractExpiration(token).getTime();

        if (username == null || username.isBlank()) {
            return unauthorized(exchange);
        }

        return isBlackListed(jti).flatMap(isBlacklisted -> {
            if (isBlacklisted) {
                log.info("Token is blacklisted session is already expired.");
                return unauthorized(exchange);
            }

            UserPrincipal principal = new UserPrincipal(
                    username, null, branchCode, jti, expirationTime, Collections.emptyList()
            );

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    principal, null, principal.getAuthorities()
            );

            return chain.filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
        });
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    private Mono<Boolean> isBlackListed(String jti) {
        return redisTemplate.hasKey("blacklist:" + jti)
                .onErrorResume(ex -> {
                    log.error("Redis unavailable. Skipping blacklist check.", ex);
                    // FAIL OPEN
                    return Mono.just(false);
                });
    }
}