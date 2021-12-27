package com.iperka.vacations.api;

import java.util.Arrays;
import java.util.List;

import com.iperka.vacations.api.security.Scopes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.servers.Server;

/**
 * OpenAPI SpringDoc Configuration Bean.
 * 
 * @author Michael Beutler
 * @version 0.0.8
 * @since 2021-12-15
 */
@Configuration
public class OpenApiConfig {
    @Value("${auth0.domain}")
    private String domain;

    @Value("${auth0.audience}")
    private String audience;

    @Value("${api.serverUrl}")
    private String apiServerUrl;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    public static final String OAUTH2 = "OAuth2";
    public static final String APPLICATION_JSON = "application/json";

    @Bean
    public OpenAPI customOpenAPI() {
        // @formatter:off
        final OpenAPI openAPI = new OpenAPI();

        final Server server = new Server();
        if (activeProfile.equals("development")) {
            server.url("http://localhost:8080/");
            server.description("Local Development Server");
        } else {
            server.url(apiServerUrl);
            server.description("Main Production Server");
        }

        openAPI
        .addSecurityItem(new SecurityRequirement().addList(OAUTH2, Arrays.asList(Scopes.ORGANIZATIONS_READ, Scopes.ORGANIZATIONS_WRITE, Scopes.ORGANIZATIONS_ALL_READ, Scopes.ORGANIZATIONS_ALL_WRITE)))
        .components(new Components()
        .addSecuritySchemes(OAUTH2,
        new SecurityScheme()
            .type(Type.OAUTH2)
            .flows(new OAuthFlows()
                .implicit(new OAuthFlow()
                    .authorizationUrl(domain + "authorize?audience=" + audience)
                    .scopes(
                       new io.swagger.v3.oas.models.security.Scopes()
                       .addString(Scopes.ORGANIZATIONS_READ, "Read owned organizations.")
                       .addString(Scopes.ORGANIZATIONS_WRITE, "Create, update and delete owned organizations.") 
                       .addString(Scopes.ORGANIZATIONS_ALL_READ, "Read all organizations.") 
                       .addString(Scopes.ORGANIZATIONS_ALL_WRITE, "Create, update and delete all organizations.") 
                    )
                )
            )
        )).security(List.of(new SecurityRequirement().addList(OAUTH2)))
        .info(new Info()
        .title("Vacations API")
        .description("REST API for iperka vacations solution.")
        .version("v1")
        .contact(new Contact().name("Support").email("support@iperka.com").url("https://iperka.com")))
        .addServersItem(server);

        return openAPI;
        // @formatter:on
    }
}
