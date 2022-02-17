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
public class InternalServerErrorResponse extends GenericResponse<String> {
    public InternalServerErrorResponse() {
        super(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Schema(example = "null")
    @Override
    public String getData() {
        return super.getData();
    }

    @Schema(example = "[{\"type\": \"NullPointerException\",\"message\": \"The server has been blown up!\",\"cause\": \"Non binary gender selected by user.\",\"code\": 500,\"field\": null}]")
    @Override
    public List<APIError> getErrors() {
        return super.getErrors();
    }

    @Schema(example = "Internal Server Error")
    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Schema(example = "null")
    @Override
    public Metadata getMetadata() {
        return super.getMetadata();
    }

    @Schema(example = "500")
    @Override
    public int getStatus() {
        return super.getStatus();
    }

}
