package com.iperka.vacations.api;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.servers.Server;

/**
 * OpenAPI SpringDoc Configuration Bean.
 * 
 * @author Michael Beutler
 * @version 0.0.2
 * @since 2021-12-15
 */
@Configuration
public class OpenApiConfig {
    @Value("${auth0.domain}")
    private String domain;

    @Bean
    public OpenAPI customOpenAPI() {
        // @formatter:off
        OpenAPI openAPI = new OpenAPI();

        openAPI
        .addSecurityItem(new SecurityRequirement().addList("oauth2", Arrays.asList("organizations:read", "organizations:write")))
        .components(new Components()
        .addSecuritySchemes("oauth2",
        new SecurityScheme()
            .type(Type.OAUTH2)
            .flows(new OAuthFlows()
                .implicit(new OAuthFlow()
                    .authorizationUrl(domain + "authorize")
                    .tokenUrl(domain + "oauth/token")
                    .scopes(
                       new Scopes()
                       .addString("organizations:read", "Read owned organizations.")
                       .addString("organizations:write", "Create, update and delete owned organizations.") 
                    )
                )
            ).in(In.HEADER)
            .bearerFormat("JWT")
            .name("OAuth2")
        ))
        .info(new Info()
        .title("Vacations API")
        .description("REST API for iperka vacations solution.")
        .version("v1")
        .contact(new Contact().name("Support").email("support@iperka.com")))
        .addServersItem(new Server().url("https://api.vacations.iperka.com/v1/").description("Main Production Server"))
        .addServersItem(new Server().url("https://api.iperka.com/vacations/v1/").description("Production Server Alias"))
        .addServersItem(new Server().url("http://localhost:8080/").description("Local Test Server"));

        return openAPI;
        // @formatter:on
    }
}
