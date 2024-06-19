package com.rgutierrez.carregistry.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI CarRegistryAPI(){
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement())
                .components(new Components().addSecuritySchemes("JavaInUseSecurityScheme", new SecurityScheme().name("JavaInUseSecurityScheme")
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")))
                .info(new Info().title("Car Registry API").version("v0.0.1"));
    }
}