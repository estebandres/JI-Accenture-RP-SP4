package com.mindhub.rp_sp3.api_gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/users/**")
                        .uri("lb://users-service"))
                .route(r -> r.path("/products/**")
                        .uri("lb://products-service"))
                .route(r -> r.path("/orders/**")
                        .uri("lb://orders-service"))
                .build();
    }
}
