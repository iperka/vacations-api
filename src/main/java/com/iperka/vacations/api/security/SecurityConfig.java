package com.iperka.vacations.api.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * The {@link com.iperka.vacations.api.security.SecurityConfig} class defines
 * the security configuration for each route. It uses the Auth0 SDK for
 * validating given JWT's.
 * 
 * @author Michael Beutler
 * @version 0.0.1
 * @since 2021-09-29
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${auth0.audience}")
    private String audience;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuer;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        /*
         * Demo configuration:
         *
         * - GET /public: available for non-authenticated requests
         * 
         * - GET /private: available for authenticated requests containing an Access
         * Token with no additional scopes
         * 
         * - GET /private-scoped: available for authenticated requests containing an
         * Access Token with the read:vacations scope granted
         */

        // @formatter:off
        http
            .httpBasic().disable()  
            .formLogin(AbstractHttpConfigurer::disable)  
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeRequests(authorize -> authorize  
                // .mvcMatchers(HttpMethod.GET, "/public").permitAll()  
                .mvcMatchers(HttpMethod.GET, "/private").authenticated()
                .mvcMatchers(HttpMethod.GET, "/private-scoped").hasAuthority("SCOPE_read:vacations")
                .anyRequest().authenticated()  
            ) 
            .cors().configurationSource(corsConfigurationSource())
            .and().oauth2ResourceServer()
            .authenticationEntryPoint(new CustomOAuth2AuthenticationEntryPoint())
            .accessDeniedHandler(new CustomOAuth2AccessDeniedHandler())
            .jwt();
        // @formatter:on
    }

    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedMethods(List.of(HttpMethod.GET.name(), HttpMethod.PUT.name(), HttpMethod.POST.name(),
                HttpMethod.DELETE.name()));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration.applyPermitDefaultValues());
        return source;
    }

    @Bean
    JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder) JwtDecoders.fromOidcIssuerLocation(issuer);

        OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(audience);
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);

        jwtDecoder.setJwtValidator(withAudience);

        return jwtDecoder;
    }
}