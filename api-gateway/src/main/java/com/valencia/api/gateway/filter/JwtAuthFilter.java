package com.valencia.api.gateway.filter;

/*
    Checar si en cache tiene informacion sobre el usuario
    Si tiene informacion obtener el token,
    comprobar que exista en la bd y regresar usuario

    Si no tiene informacion del token
        Poner en cache info del token
    Que intente loguearse, ir al servicio de usuario,
    Verificar que exista
    Comprobar que el token sea correcto
        Dar acceso
    Si no tiene acceso, pedor que se autentique
    salvar el token

 */


import com.valencia.api.gateway.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

public class JwtAuthFilter implements GlobalFilter {
    @Autowired
    private JwtService jwtService;

    private static final List<String> openEndpoints = List.of("/auth/login", "/auth/register");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().toString();
        if (openEndpoints.contains(path)) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);
        if (!jwtService.isTokenValid(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);

    }
}
