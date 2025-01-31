package com.mindhub.rp_sp3.api_gateway.config;

import com.mindhub.rp_sp3.api_gateway.config.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public RouteLocator customRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/auth/**")
                        .uri("lb://users-service"))
                .route(r -> r.path("/users/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter))
                        .uri("lb://users-service"))
                .route(r -> r.path("/products/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter))
                        .uri("lb://products-service"))
                .route(r -> r.path("/orders/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter))
                        .uri("lb://orders-service"))
                .build();
    }
}
