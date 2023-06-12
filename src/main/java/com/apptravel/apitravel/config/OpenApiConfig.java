package com.apptravel.apitravel.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Best Travel API",
                version = "1.0",
                description = "Documentation for the best travel API"
        )
)
public class OpenApiConfig {



}
