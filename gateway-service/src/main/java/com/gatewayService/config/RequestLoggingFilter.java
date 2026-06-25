package com.gatewayService.config;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.MDC;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;

import org.springframework.core.Ordered;

import org.springframework.http.server.reactive.ServerHttpRequest;

import org.springframework.stereotype.Component;

import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Component
public class RequestLoggingFilter implements GlobalFilter, Ordered {

    private static final String CORRELATION_ID = "correlationId";

    private static final String CORRELATION_HEADER = "X-Correlation-ID";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        long startTime = System.currentTimeMillis();

        String requestCorrelationId =

                exchange.getRequest().getHeaders().getFirst(CORRELATION_HEADER);

        final String correlationId =

                requestCorrelationId != null

                        ? requestCorrelationId

                        : UUID.randomUUID().toString();

        ServerHttpRequest mutatedRequest =

                exchange.getRequest().mutate()

                        .header(CORRELATION_HEADER, correlationId)
                        .header("X-Gateway-Service","gateway-service")

                        .build();

        ServerWebExchange mutatedExchange =

                exchange.mutate().request(mutatedRequest).build();

        MDC.put(CORRELATION_ID, correlationId);

        return chain.filter(mutatedExchange)

                .doFinally(signalType -> {

                    long duration =

                            System.currentTimeMillis() - startTime;

                    log.info("REQ-ID={} METHOD={} URI={} STATUS={} TIME={}ms", correlationId, mutatedRequest.getMethod(), mutatedRequest.getURI().getPath(), mutatedExchange.getResponse().getStatusCode(), duration);

                    MDC.clear();
                });
    }

    @Override
    public int getOrder() {

        return -1;
    }
}