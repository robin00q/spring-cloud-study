package com.example.gatewaystudy.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {
    public LoggingFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        // Custom Pre Filter
//        return (exchange, chain) -> {
//            final ServerHttpRequest request = exchange.getRequest();
//            final ServerHttpResponse response = exchange.getResponse();
//
//            log.info("Global Filter baseMessage: {}", com.example.servicegateway.config.getBaseMessage());
//
//            if(com.example.servicegateway.config.isPreLogger()) {
//                log.info("Global Filter Start: request id -> {}", request.getId());
//            }
//
//            return chain.com.example.servicegateway.filter(exchange).then(Mono.fromRunnable(() -> {
//                if(com.example.servicegateway.config.isPostLogger()) {
//                    log.info("Global Filter End: response code -> {}", response.getStatusCode());
//                }
//            }));
//        };

        GatewayFilter filter = new OrderedGatewayFilter((exchange, chain) -> {
            final ServerHttpRequest request = exchange.getRequest();
            final ServerHttpResponse response = exchange.getResponse();

            log.info("Logging Filter baseMessage: {}", config.getBaseMessage());
            if(config.isPreLogger()) {
                log.info("Logging PRE Filter: request id -> {}", request.getId());
            }

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                if(config.isPostLogger()) {
                    log.info("Logging POST Filter: response code -> {}", response.getStatusCode());
                }
            }));
        }, Ordered.LOWEST_PRECEDENCE);

        return filter;
    }

    @Data
    public static class Config {
        // Put the Configuration Properties
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }
}
