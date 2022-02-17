package com.iperka.vacations.api.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iperka.vacations.api.helpers.APIError;
import com.iperka.vacations.api.helpers.GenericResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;
import org.springframework.security.web.access.AccessDeniedHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * Overrides default handler class provided by Spring Security 5. Converts the
 * AccessDeniedException into a Generic Response with the APIError.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
public class CustomOAuth2AccessDeniedHandler implements AccessDeniedHandler {

    /**
     * Handler function for built in Exception.
     * 
     * @since 1.0.0
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e)
            throws IOException {
        // TODO: Probably log?
        log.warn("Request from user {} has been blocked due to insufficient privileges.", request.getRemoteAddr());

        // Extract fields
        String errorMessage = e.getLocalizedMessage();
        String cause = null;

        // Apply custom error message and cause
        if (request.getUserPrincipal() instanceof AbstractOAuth2TokenAuthenticationToken) {
            errorMessage = "The request requires higher privileges than provided by the access token.";
            cause = "The authenticated user has not been granted the required scope(s).";
        }

        ObjectMapper mapper = new ObjectMapper();
        GenericResponse<?> responseObject = new GenericResponse<>(HttpStatus.FORBIDDEN);
        responseObject.addError(new APIError("OAuthException", errorMessage, cause, 403));

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(mapper.writeValueAsString(responseObject));
    }
}
