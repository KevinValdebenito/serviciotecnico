package com.serviciotecnico.cotizacion.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/**
 * Configuración de Swagger/OpenAPI.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI cotizacionOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Cotizaciones")
                        .version("1.0")
                        .description("CRUD de cotizaciones y validación remota de tickets."));
    }
}
