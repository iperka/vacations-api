package com.iperka.vacations.api.audit;

import java.util.List;


import com.iperka.vacations.api.audit.exceptions.AuditNotFoundException;
import com.iperka.vacations.api.config.OpenApiConfig;
import com.iperka.vacations.api.helpers.GenericResponse;
import com.iperka.vacations.api.helpers.openapi.responses.ForbiddenResponse;
import com.iperka.vacations.api.helpers.openapi.responses.InternalServerErrorResponse;
import com.iperka.vacations.api.helpers.openapi.responses.UnauthorizedResponse;
import com.iperka.vacations.api.security.Scopes;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;

/**
 * RestController endpoint for /audit logs route.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@RestController
@Validated
@RequestMapping(value = "/audits", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuditController {

    @Autowired
    private AuditService auditService;

    /**
     * Index route for /audit logs endpoint. Returns all audit logs (if user
     * is authorized).
     * 
     * @since 1.0.0
     * @param authentication Will be provided by Spring Security.
     * @param pageable       Adds built in pagination.
     * @return A generic Response with a list of all audit logs as data property.
     */
    @GetMapping
    // @formatter:off
    @Operation(
        summary = "Finds all audit logs.", 
        description = "Finds all audit logs.", 
        security = {
            @SecurityRequirement(
                name = OpenApiConfig.OAUTH2,
                scopes = {Scopes.AUDITS_ALL_READ,Scopes.AUDITS_ALL_WRITE}
            )
        }, 
        tags = {"Audit Logs"}, 
        responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = AuditListResponse.class))),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = UnauthorizedResponse.class))),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = ForbiddenResponse.class))),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = InternalServerErrorResponse.class)))
        }
    )
    // @formatter:on
    public ResponseEntity<GenericResponse<List<Audit>>> findAll(
    // @formatter:off
        final Authentication authentication,
        @ParameterObject @RequestParam(name = "objectId", required = false) final String objectId,
        @ParameterObject @PageableDefault(size = 20, sort = "createdAt", direction = Direction.DESC) final Pageable pageable
     // @formatter:on
    ) {
        // TODO: Evaluate permissions.
        Page<Audit> page;
        if (objectId != null) {
            page = auditService.findAllByObjectId(objectId, pageable);
        } else {
            page = auditService.findAll(pageable);
        }
        return GenericResponse.<Audit>fromPage(HttpStatus.OK, page, null).build();
    }

    /**
     * Index route for /audit logs endpoint. Returns auditLog with given id.
     * 
     * @since 1.0.0
     * @param authentication Will be provided by Spring Security.
     * @param id           Id of demanded auditLog object.
     * @return A generic Response with auditLog as data property.
     */
    @GetMapping(value = "/{id}")
    // @formatter:off
    @Operation(
        summary = "Finds auditLog with given id.", 
        description = "Finds auditLog with given id.", 
        security = {
            @SecurityRequirement(
                name = OpenApiConfig.OAUTH2,
                scopes = {Scopes.AUDITS_ALL_READ,Scopes.AUDITS_ALL_WRITE}
            )
        }, 
        tags = {"Audit Logs"}, 
        responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = AuditResponse.class))),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = UnauthorizedResponse.class))),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = ForbiddenResponse.class))),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON)),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = InternalServerErrorResponse.class)))
        }
    )
    // @formatter:on
    public ResponseEntity<GenericResponse<Audit>> findById(
    // @formatter:off
        final Authentication authentication,
        @PathVariable("id") final String id
     // @formatter:on
    ) {
        GenericResponse<Audit> response = new GenericResponse<>(HttpStatus.OK);

        try {
            Audit auditLog = auditService.findById(id);
            response.setData(auditLog);
        } catch (AuditNotFoundException e) {
            log.info("Audit could not be found.");
            return response.fromError(HttpStatus.NOT_FOUND, e.toApiError()).build();
        }

        return response.build();
    }

    /**
     * Helper class for OpenAPI generation.
     * 
     * @author Michael Beutler
     * @version 1.0.0
     * @since 1.0.0
     */
    private final class AuditListResponse extends GenericResponse<List<Audit>> {

        /**
         * Default Constructor required by Java.
         * 
         * @param status HTTP Status.
         */
        public AuditListResponse(final HttpStatus status) {
            super(status);
        }
    };

    /**
     * Helper class for OpenAPI generation.
     * 
     * @author Michael Beutler
     * @version 1.0.0
     * @since 1.0.0
     */
    private final class AuditResponse extends GenericResponse<Audit> {

        /**
         * Default Constructor required by Java.
         * 
         * @param status HTTP Status.
         */
        public AuditResponse(final HttpStatus status) {
            super(status);
        }
    };
}
