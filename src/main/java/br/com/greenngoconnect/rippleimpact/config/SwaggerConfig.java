package br.com.greenngoconnect.rippleimpact.config;

import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI api() {
        final String schemeName = "bearerAuth";
        SecurityScheme bearer = new SecurityScheme()
                .name(schemeName)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        return new OpenAPI()
                .info(new Info()
                        .title("OakShield - API")
                        .version("v1")
                        .description("API documentation for OakShield, a security platform for managing assets, incidents, and audits."))
                .components(new Components().addSecuritySchemes(schemeName, bearer))
                .addSecurityItem(new SecurityRequirement().addList(schemeName))
                .servers(List.of(new Server().url("/api")));
    }
}

