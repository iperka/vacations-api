package com.iperka.vacations.api.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iperka.vacations.api.helpers.APIError;
import com.iperka.vacations.api.helpers.GenericResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;

import lombok.extern.slf4j.Slf4j;

/**
 * Overrides default handler class provided by Spring Security 5. Converts the
 * AuthenticationEntryPoint into a Generic Response with the APIError. This
 * exception suggests a missing Barer token or invalid token in general.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
public class CustomOAuth2AuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Handler function for built in Exception.
     * 
     * @since 1.0.0
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException {
        // TODO: Probably log?
        log.warn("Request from user {} has been blocked due to insufficient authentication.",
                request.getRemoteAddr());

        // Apply custom error message
        String errorMessage = "Insufficient authentication details.";
        if (e instanceof InvalidBearerTokenException) {
            // Extract field
            errorMessage = e.getLocalizedMessage();
        }

        ObjectMapper mapper = new ObjectMapper();
        GenericResponse<?> responseObject = new GenericResponse<>(HttpStatus.UNAUTHORIZED);
        responseObject.addError(new APIError("OAuthException", errorMessage,
                "Missing or invalid Bearer Token in Authentication header.", 401));

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(mapper.writeValueAsString(responseObject));
    }
}
