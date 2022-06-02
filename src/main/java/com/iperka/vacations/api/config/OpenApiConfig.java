package com.iperka.vacations.api.config;

import java.util.Arrays;
import java.util.List;

import com.iperka.vacations.api.VacationsApiApplication;
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
 * OpenAPI SpringDoc Configuration bean.
 * 
 * @author Michael Beutler
 * @version 1.0.2
 * @since 1.0.0
 */
@Configuration
public class OpenApiConfig {
    @Value("${auth0.domain}")
    private String domain;

    @Value("${auth0.audience}")
    private String audience;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    public static final String OAUTH2 = "OAuth2";
    public static final String APPLICATION_JSON = "application/json";
    public static final String SERVER_DESCRIPTION_PRODUCTION = "Production";
    public static final String SERVER_DESCRIPTION_STAGING = "Staging";
    public static final String SERVER_DESCRIPTION_DEVELOPMENT = "Development";

    @Bean
    public OpenAPI customOpenAPI() {
        // @formatter:off
        final OpenAPI openAPI = new OpenAPI();

        if (activeProfile.equals("development")) {
            openAPI.addServersItem(new Server().url("http://localhost:8080/").description("Local Development"));
        }

        openAPI
        .addSecurityItem(new SecurityRequirement().addList(OAUTH2, Arrays.asList(
            Scopes.VACATIONS_READ, 
            Scopes.VACATIONS_WRITE, 
            Scopes.VACATIONS_ALL_READ, 
            Scopes.VACATIONS_ALL_WRITE,
            Scopes.USERS_READ,
            Scopes.USERS_WRITE,
            Scopes.USERS_ALL_READ,
            Scopes.USERS_ALL_WRITE,
            Scopes.AUDITS_ALL_READ,
            Scopes.AUDITS_ALL_WRITE
        )))
        .components(new Components()
        .addSecuritySchemes(OAUTH2,
        new SecurityScheme()
            .type(Type.OAUTH2)
            .flows(new OAuthFlows()
                .implicit(new OAuthFlow()
                    .authorizationUrl(domain + "authorize?audience=" + audience)
                    .scopes(
                       new io.swagger.v3.oas.models.security.Scopes()
                       .addString(Scopes.VACATIONS_READ, "Read owned vacations.")
                       .addString(Scopes.VACATIONS_WRITE, "Create, update and delete owned vacations.") 
                       .addString(Scopes.VACATIONS_ALL_READ, "Read all vacations.") 
                       .addString(Scopes.VACATIONS_ALL_WRITE, "Create, update and delete all vacations.") 
                       .addString(Scopes.USERS_READ, "Read basic user metadata.") 
                       .addString(Scopes.USERS_WRITE, "Update basic user metadata.") 
                       .addString(Scopes.USERS_ALL_READ, "Read all users.") 
                       .addString(Scopes.USERS_ALL_WRITE, "Create, update and delete all users.") 
                       .addString(Scopes.AUDITS_ALL_READ, "Read all audit logs.") 
                       .addString(Scopes.AUDITS_ALL_WRITE, "Create, update and delete all audit logs.") 
                    )
                )
            )
        )).security(List.of(new SecurityRequirement().addList(OAUTH2)))
        .info(new Info()
        .title("vacations API")
        .description("REST API for vacations solution by iperka.")
        .version("v" + VacationsApiApplication.class.getPackage().getImplementationVersion())
        .contact(new Contact().name("Support").email("support@iperka.com").url("https://iperka.com")))
        .addServersItem(new Server().url("https://api.vacations.iperka.com/v1/").description(SERVER_DESCRIPTION_PRODUCTION))
        .addServersItem(new Server().url("https://api.vacations.iperka.ch/v1/").description(SERVER_DESCRIPTION_PRODUCTION));

        return openAPI;
        // @formatter:on
    }
}