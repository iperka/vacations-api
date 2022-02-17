package com.iperka.vacations.api.helpers.openapi.responses;

import java.util.List;

import com.iperka.vacations.api.helpers.APIError;
import com.iperka.vacations.api.helpers.GenericResponse;
import com.iperka.vacations.api.helpers.Metadata;

import org.springframework.http.HttpStatus;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Helper class for providing better OpenAPI specifications.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 1.0.0
 */
public class UnauthorizedResponse extends GenericResponse<String> {
    public UnauthorizedResponse() {
        super(HttpStatus.UNAUTHORIZED);
    }

    @Schema(example = "null")
    @Override
    public String getData() {
        return super.getData();
    }

    @Schema(example = "[{\"type\": \"OAuthException\",\"message\": \"Insufficient authentication details.\",\"cause\": \"Missing or invalid Bearer Token in Authentication header.\",\"code\": 401,\"field\": null}]")
    @Override
    public List<APIError> getErrors() {
        return super.getErrors();
    }

    @Schema(example = "Unauthorized")
    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Schema(example = "null")
    @Override
    public Metadata getMetadata() {
        return super.getMetadata();
    }

    @Schema(example = "401")
    @Override
    public int getStatus() {
        return super.getStatus();
    }

}
