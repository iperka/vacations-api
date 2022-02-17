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
public class UpdatedResponse<T> extends GenericResponse<T> {
    public UpdatedResponse() {
        super(HttpStatus.NO_CONTENT);
    }

    @Schema(example = "[]")
    @Override
    public List<APIError> getErrors() {
        return super.getErrors();
    }

    @Schema(example = "No Content")
    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Schema(example = "null")
    @Override
    public Metadata getMetadata() {
        return super.getMetadata();
    }

    @Schema(example = "204")
    @Override
    public int getStatus() {
        return super.getStatus();
    }
}