package com.iperka.vacations.api.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iperka.vacations.api.helpers.APIError;
import com.iperka.vacations.api.helpers.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * The {@link com.iperka.vacations.api.security.CustomOAuth2AccessDeniedHandler}
 * class handels requests with insufficient scope privileges.
 * 
 * TODO: Extend Error message object.
 * 
 * @author Michael Beutler
 * @version 0.0.2
 * @since 2021-09-29
 */
public class CustomOAuth2AccessDeniedHandler implements AccessDeniedHandler {
    public static final Logger logger = LoggerFactory.getLogger(CustomOAuth2AccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e)
            throws IOException {
        logger.warn("Request from user {} has been blocked due to insufficient privilegs.", request.getRemoteAddr());

        String errorMessage = e.getLocalizedMessage();
        String cause = null;
        if (request.getUserPrincipal() instanceof AbstractOAuth2TokenAuthenticationToken) {
            errorMessage = "The request requires higher privileges than provided by the access token.";
            cause = "The authenticated user has not been granted the required scope(s).";
        }

        ObjectMapper mapper = new ObjectMapper();
        Response<?> responseObject = new Response<>(HttpStatus.FORBIDDEN);
        responseObject.addError(new APIError("OAuthException", errorMessage, cause, 403));

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(mapper.writeValueAsString(responseObject));
    }
}
