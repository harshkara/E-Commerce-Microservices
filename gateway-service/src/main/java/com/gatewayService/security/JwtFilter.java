package com.gatewayService.security;

import com.common.constants.PublicRoutes;
import com.common.security.JwtService;
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
@Component("gatewayJwtConfig")
public class JwtFilter implements WebFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;
    private final ReactiveRedisTemplate<String, String> redisTemplate;

    public JwtFilter(JwtService jwtService,
                     ReactiveRedisTemplate<String, String> redisTemplate) {
        this.jwtService = jwtService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        log.info("========== JWT FILTER START ==========");

        String path = exchange.getRequest().getURI().getPath();
        log.info("Request Path : {}", path);

        for (String route : PublicRoutes.ROUTES) {
            if (path.startsWith(route)) {
                log.info("Public API. Skipping JWT validation.");
                return chain.filter(exchange);
            }
        }

        String authHeader =
                exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            log.info("Authorization header missing.");
            return unauthorized(exchange);
        }

        log.info("Authorization header received.");

        String token = authHeader.substring(BEARER_PREFIX.length());

        if (!jwtService.validateToken(token)) {
            log.info("Token validation failed.");
            return unauthorized(exchange);
        }

        log.info("Token validation successful.");

        String username = jwtService.extractUserName(token);
        String branchCode = jwtService.extractBranchCode(token);
        String jti = jwtService.extractJti(token);
        long expirationTime = jwtService.extractExpiration(token).getTime();

        log.info("Username      : {}", username);
        log.info("Branch Code   : {}", branchCode);
        log.info("JTI           : {}", jti);
        log.info("Expiry Time   : {}", expirationTime);

        if (username == null || username.isBlank()) {
            log.info("Username not found inside token.");
            return unauthorized(exchange);
        }

        return isBlackListed(jti)
                .flatMap(isBlacklisted -> {

                    log.info("Blacklist Result : {}", isBlacklisted);

                    if (isBlacklisted) {
                        log.info("Token is blacklisted.");
                        return unauthorized(exchange);
                    }

                    log.info("Token is NOT blacklisted.");

                    UserPrincipal principal = new UserPrincipal(
                            username,
                            null,
                            branchCode,
                            jti,
                            expirationTime,
                            Collections.emptyList()
                    );

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    principal,
                                    null,
                                    principal.getAuthorities()
                            );

                    log.info("Authentication stored in Security Context.");
                    log.info("========== JWT FILTER END ==========");

                    return chain.filter(exchange)
                            .contextWrite(
                                    ReactiveSecurityContextHolder.withAuthentication(authentication)
                            );
                });
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    private Mono<Boolean> isBlackListed(String jti) {

        String key = "blacklist:" + jti;

        log.info("Checking Redis Key : {}", key);

        return redisTemplate.hasKey(key)
                .doOnNext(exists ->
                        log.info("Redis has key '{}': {}", key, exists))
                .onErrorResume(ex -> {
                    log.error("Redis unavailable while checking blacklist.", ex);
                    return Mono.just(false);
                });
    }
}