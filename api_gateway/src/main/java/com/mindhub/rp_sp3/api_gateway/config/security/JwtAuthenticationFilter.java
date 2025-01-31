package com.mindhub.rp_sp3.api_gateway.config.security;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class JwtAuthenticationFilter implements GatewayFilter {

    @Autowired
    private JwtUtils jwtUtil;


    public JwtAuthenticationFilter(JwtUtils jwtUtils){
        this.jwtUtil = jwtUtils;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            Date string = jwtUtil.parseClaims(token).getExpiration();
            try {
                if (!jwtUtil.isTokenExpired(token)) {
                    Claims claims = jwtUtil.parseClaims(token);
                    System.out.println("STEVE: claims: " + claims);
                    String path = exchange.getRequest().getPath().toString();

                    List<String> roles = (List<String>) claims.get("roles");

                    if (path.startsWith("/users/") || path.startsWith("/orders/")) {
                        if (!roles.isEmpty() && roles.contains("ADMIN")) {
                            return chain.filter(exchange); // Allow access with ADMIN role
                        } else {
                            return onError(exchange, "Unauthorized: Not enough privileges", HttpStatus.UNAUTHORIZED); // Unauthorized for non-ADMIN users
                        }
                    }

                    // Check if the path requires USER role
                    if (path.equals("/users/self") || path.equals("/orders/mine")) {
                        if (!roles.isEmpty() && roles.contains("USER")) {
                            return chain.filter(exchange); // Allow access with USER role
                        } else {
                            return onError(exchange, "Unauthorized: Not enough privileges", HttpStatus.UNAUTHORIZED); // Unauthorized for non-USER users
                        }
                    }
//                        exchange.getRequest().mutate().header("username", claims.getSubject()).build();
                } else {
                    return onError(exchange, "Invalid JWT Token", HttpStatus.UNAUTHORIZED);
                }
            } catch (Exception e) {
                return onError(exchange, "JWT Token validation failed", HttpStatus.UNAUTHORIZED);
            }
        } else {
            return onError(exchange, "Authorization header is missing or invalid", HttpStatus.UNAUTHORIZED);
        }
        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        exchange.getResponse().setStatusCode(httpStatus);
        return exchange.getResponse().setComplete();
    }

    private List<GrantedAuthority> extractAuthorities(Claims claims) {
        // Extract and return authorities from claims
        return new ArrayList<>();
    }
}