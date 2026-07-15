/**
 * Configuración de rutas del API Gateway.
 *
 * <p>Define hacia qué microservicio se reenvía cada petición entrante.
 * El Gateway es la única puerta de entrada externa del sistema: desde
 * afuera solo se conocen estas rutas, nunca las URLs internas de cada
 * microservicio de dominio. Los encabezados de la petición original
 * (incluido Authorization) se reenvían automáticamente al hacer el proxy.</p>
 */
package com.serviciotecnico.gateway.config;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.web.servlet.function.RequestPredicates.path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class GatewayRoutesConfig {

    @Value("${auth.service.url}")
    private String authServiceUrl;

    @Value("${bff.service.url}")
    private String bffServiceUrl;

    @Bean
    public RouterFunction<ServerResponse> gatewayRoutes() {
        return route("auth-service")
                .route(path("/register").or(path("/login")), http())
                .before(uri(authServiceUrl))
                .build()
            .and(
                route("bff-service")
                    .route(path("/api/bff/**"), http())
                    .before(uri(bffServiceUrl))
                    .build()
            );
    }
}