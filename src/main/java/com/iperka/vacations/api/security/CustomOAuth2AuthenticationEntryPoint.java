package com.iperka.vacations.api.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iperka.vacations.api.helpers.APIError;
import com.iperka.vacations.api.helpers.Response;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;

import lombok.extern.slf4j.Slf4j;

/**
 * The {@link com.iperka.vacations.api.security.CustomOAuth2AccessDeniedHandler}
 * class handels requests with insufficient authentication.
 * 
 * TODO: Extend Error message object.
 * 
 * @author Michael Beutler
 * @version 0.0.2
 * @since 2021-09-29
 */
@Slf4j
public class CustomOAuth2AuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException {
        log.warn("Request from user {} has been blocked due to insufficient authentication.",
                request.getRemoteAddr());

        String errorMessage = "Insufficient authentication details.";
        if (e instanceof InvalidBearerTokenException) {
            errorMessage = e.getLocalizedMessage();
        }

        ObjectMapper mapper = new ObjectMapper();
        Response<?> responseObject = new Response<>(HttpStatus.UNAUTHORIZED);
        responseObject.addError(new APIError("OAuthException", errorMessage,
                "Missing or invalid Bearer Token in Authentication header.", 401));

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(mapper.writeValueAsString(responseObject));
    }
}
