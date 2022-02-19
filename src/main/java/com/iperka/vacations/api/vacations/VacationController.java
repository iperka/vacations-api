package com.iperka.vacations.api.vacations;

import java.time.Year;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import com.iperka.vacations.api.config.OpenApiConfig;
import com.iperka.vacations.api.helpers.GenericResponse;
import com.iperka.vacations.api.helpers.openapi.responses.BadRequestResponse;
import com.iperka.vacations.api.helpers.openapi.responses.ConflictResponse;
import com.iperka.vacations.api.helpers.openapi.responses.CreatedResponse;
import com.iperka.vacations.api.helpers.openapi.responses.ForbiddenResponse;
import com.iperka.vacations.api.helpers.openapi.responses.InternalServerErrorResponse;
import com.iperka.vacations.api.helpers.openapi.responses.InvalidCaptchaResponse;
import com.iperka.vacations.api.helpers.openapi.responses.UnauthorizedResponse;
import com.iperka.vacations.api.helpers.openapi.responses.UpdatedResponse;
import com.iperka.vacations.api.security.Helpers;
import com.iperka.vacations.api.security.Scopes;
import com.iperka.vacations.api.vacations.dto.VacationDTO;
import com.iperka.vacations.api.vacations.exceptions.VacationNotFoundException;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * RestController endpoint for /vacations route.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@RestController
@Validated
@RequestMapping(value = "/vacations", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Vacations", description = "Endpoints for CRUD operations with vacations.")
public class VacationController {

    @Autowired
    private VacationService vacationService;

    /**
     * Index route for /vacations endpoint. Returns all vacations (if user is
     * authorized).
     * 
     * @since 1.0.0
     * @param authentication Will be provided by Spring Security.
     * @param pageable       Adds built in pagination.
     * @return A generic Response with a list of all vacations as data property.
     */
    @GetMapping
    // @formatter:off
    @Operation(
        summary = "Finds all vacations.", 
        // TODO: Extend description.
        description = "Finds all vacations owned by authenticated user.", 
        security = {
            @SecurityRequirement(
                name = OpenApiConfig.OAUTH2,
                scopes = {Scopes.VACATIONS_READ, Scopes.VACATIONS_WRITE, Scopes.VACATIONS_ALL_READ, Scopes.VACATIONS_ALL_WRITE}
            )
        }, 
        tags = {"Vacations"}, 
        responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = VacationListResponse.class))),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = UnauthorizedResponse.class))),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = ForbiddenResponse.class))),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = InternalServerErrorResponse.class)))
        }
    )
    // @formatter:on
    public ResponseEntity<GenericResponse<List<Vacation>>> findAll(
    // @formatter:off
        final Authentication authentication,
        @ParameterObject @PageableDefault(size = 20, sort = "createdAt") final Pageable pageable,
        @RequestParam(required = false) @Parameter(description = "Filter vacations by owner.") final String owner
     // @formatter:on
    ) {
        final String userId = Helpers.getUserId(authentication);
        String query = null;

        // Check if owner is set
        if (StringUtils.hasText(owner)) {
            query = String.format("owner=*%s*", owner);
        }

        Page<Vacation> page;
        // Check if authenticated user has been granted vacations:all:read
        if (Helpers.hasScope(Scopes.VACATIONS_ALL_READ, authentication) && !StringUtils.hasText(owner)) {
            page = this.vacationService.findAll(pageable);
        } else if (Helpers.hasScope(Scopes.VACATIONS_ALL_READ, authentication) && StringUtils.hasText(owner)) {
            page = this.vacationService.findAllByOwner(pageable, owner);
        } else {
            page = this.vacationService.findAllByOwner(pageable, userId);
        }

        return GenericResponse.<Vacation>fromPage(HttpStatus.OK, page, query).build();
    }

    /**
     * Returns an overview array for given year. This allows for charts and
     * better vacation distribution visualizations.
     * 
     * @since 1.0.0
     * @param authentication Will be provided by Spring Security.
     * @param year           Year for array.
     * @param owner          Optional owner.
     * @return Vacation day count for each month as array.
     */
    @GetMapping(value = "/overview/{year}")
    // @formatter:off
    @Operation(
        summary = "Counts all vacations days by month for the given year.", 
        description = "Counts all vacations days by month for the given year owned by authenticated user (or specified).", 
        security = {
            @SecurityRequirement(
                name = OpenApiConfig.OAUTH2,
                scopes = {Scopes.VACATIONS_READ, Scopes.VACATIONS_WRITE, Scopes.VACATIONS_ALL_READ, Scopes.VACATIONS_ALL_WRITE}
            )
        }, 
        tags = {"Vacations"}, 
        responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = VacationDaysCountByMonthResponse.class))),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = UnauthorizedResponse.class))),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = ForbiddenResponse.class))),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = InternalServerErrorResponse.class)))
        }
    )
    // @formatter:on
    public ResponseEntity<GenericResponse<double[]>> getVacationCountDaysByMonth(
    // @formatter:off
        final Authentication authentication,
        @PathVariable("year") @Parameter(description = "Will only display vacations within the given year.", example = "2022", schema = @Schema(implementation = int.class)) final Year year,
        @RequestParam(required = false) @Parameter(description = "Filter vacations by owner (advanced scopes required).") final String owner
     // @formatter:on
    ) {
        final String userId = Helpers.getUserId(authentication);
        final GenericResponse<double[]> response = new GenericResponse<>(HttpStatus.OK);

        List<Vacation> vacations;
        // Check if authenticated user has been granted vacations:all:read
        if (Helpers.hasScope(Scopes.VACATIONS_ALL_READ, authentication) && !StringUtils.hasText(owner)) {
            vacations = this.vacationService.findAll(PageRequest.of(0, 100)).toList();
        } else {
            vacations = this.vacationService.findAllByOwner(PageRequest.of(0, 100), userId).toList();
        }

        response.setData(this.vacationService.getDaysCountByMonth(vacations, year));

        return response.build();
    }

    /**
     * Searches for the next vacation according to current date.
     * 
     * @since 1.0.1
     * @param authentication Will be provided by Spring Security.
     * @param owner          Optional owner.
     * @return Next vacation object or 404 error.
     */
    @GetMapping(value = "/next")
    // @formatter:off
    @Operation(
        summary = "Finds next vacation.", 
        description = "Finds next vacation for authenticated user. If there is none, it will return 404.", 
        security = {
            @SecurityRequirement(
                name = OpenApiConfig.OAUTH2,
                scopes = {Scopes.VACATIONS_READ, Scopes.VACATIONS_WRITE, Scopes.VACATIONS_ALL_READ, Scopes.VACATIONS_ALL_WRITE}
            )
        }, 
        tags = {"Vacations"}, 
        responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = VacationResponse.class))),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = UnauthorizedResponse.class))),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = ForbiddenResponse.class))),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON)),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = InternalServerErrorResponse.class)))
        }
    )
    // @formatter:on
    public ResponseEntity<GenericResponse<Vacation>> getNextVacation(
    // @formatter:off
        final Authentication authentication,
        @RequestParam(required = false) @Parameter(description = "Filter vacations by owner (advanced scopes required).") final String owner
     // @formatter:on
    ) {
        final String userId = Helpers.getUserId(authentication);
        final GenericResponse<Vacation> response = new GenericResponse<>(HttpStatus.OK);

        // Get midnight today
        Long time = new Date().getTime();
        Date date = new Date(time - time % (24 * 60 * 60 * 1000));

        try {
            Vacation vacation;
            // Check if authenticated user has been granted vacations:all:read
            if (Helpers.hasScope(Scopes.VACATIONS_ALL_READ, authentication) && StringUtils.hasText(owner)) {
                vacation = this.vacationService.findByOwnerAndStartDateGreaterThanOrderByStartDateAsc(owner,
                        date);
            } else {
                vacation = this.vacationService.findByOwnerAndStartDateGreaterThanOrderByStartDateAsc(userId,
                        date);
            }

            response.setData(vacation);
        } catch (VacationNotFoundException e) {
            return response.fromError(HttpStatus.NOT_FOUND, e.toApiError()).build();
        }

        return response.build();
    }

    /**
     * Index route for /vacations endpoint. Returns Vacations with given uuid (if
     * user
     * is authorized).
     * 
     * @since 1.0.0
     * @param authentication Will be provided by Spring Security.
     * @param uuid           Id of demanded Vacations object.
     * @return A generic Response with Vacations as data property.
     */
    @GetMapping(value = "/{uuid}")
    // @formatter:off
    @Operation(
        summary = "Finds Vacations with given uuid.", 
        // TODO: Extend description.
        description = "Finds Vacations with given uuid.", 
        security = {
            @SecurityRequirement(
                name = OpenApiConfig.OAUTH2,
                scopes = {Scopes.VACATIONS_READ, Scopes.VACATIONS_WRITE, Scopes.VACATIONS_ALL_READ, Scopes.VACATIONS_ALL_WRITE}
            )
        }, 
        tags = {"Vacations"}, 
        responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = VacationResponse.class))),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = UnauthorizedResponse.class))),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = ForbiddenResponse.class))),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON)),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = InternalServerErrorResponse.class)))
        }
    )
    // @formatter:on
    public ResponseEntity<GenericResponse<Vacation>> findByUuid(
    // @formatter:off
        final Authentication authentication,
        @PathVariable("uuid") final UUID uuid
     // @formatter:on
    ) {
        GenericResponse<Vacation> response = new GenericResponse<>(HttpStatus.OK);

        try {
            Vacation vacation = this.vacationService.findByUuid(uuid);
            response.setData(vacation);
        } catch (VacationNotFoundException e) {
            log.info("Vacation could not be found.");
            return response.fromError(HttpStatus.NOT_FOUND, e.toApiError()).build();
        }

        return response.build();
    }

    /**
     * Index route for /vacations endpoint. Creates a new Vacations. (if user is
     * authorized).
     * 
     * @since 1.0.0
     * @param authentication Will be provided by Spring Security.
     * @param vacationsDTO   Vacation object.
     * @return A generic Response with created vacations as data property.
     */
    @PostMapping
    // @RequiresCaptcha
    // @formatter:off
    @Operation(
        summary = "Creates a Vacations.", 
        // TODO: Extend description.
        description = "Creates a new Vacations.", 
        security = {
            @SecurityRequirement(
                name = OpenApiConfig.OAUTH2,
                scopes = {Scopes.VACATIONS_WRITE, Scopes.VACATIONS_ALL_WRITE}
            )
        }, 
        tags = {"Vacations"}, 
        responses = {
            @ApiResponse(description = "Created", responseCode = "201", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = VacationCreatedResponse.class))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(oneOf = {BadRequestResponse.class, InvalidCaptchaResponse.class}))),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = UnauthorizedResponse.class))),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = ForbiddenResponse.class))),
            @ApiResponse(description = "Conflict", responseCode = "409", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = ConflictResponse.class))),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = InternalServerErrorResponse.class)))
        }
    )
    // @formatter:on
    public ResponseEntity<GenericResponse<Vacation>> create(
    // @formatter:off
        final Authentication authentication,
        @Valid @RequestBody(required = true, content = @Content(schema =  @Schema(implementation = VacationDTO.class))) @org.springframework.web.bind.annotation.RequestBody final VacationDTO vacationsDTO
     // @formatter:on
    ) {
        GenericResponse<Vacation> response = new GenericResponse<>(HttpStatus.CREATED);

        Vacation vacation = vacationsDTO.toObject();
        vacation.setOwner(Helpers.getUserId(authentication));
        vacation = this.vacationService.create(vacation);
        response.setData(vacation);

        return response.build();
    }

    /**
     * Index route for /vacations endpoint. Updates an existing Vacations.
     * (if user is authorized).
     * 
     * @since 1.0.0
     * @param authentication Will be provided by Spring Security.
     * @param vacationsDTO   Vacation object.
     * @return A generic Response with created vacations as data property.
     */
    @PutMapping(value = "/{uuid}")
    // @RequiresCaptcha
    // @formatter:off
    @Operation(
        summary = "Updates a Vacations.", 
        // TODO: Extend description.
        description = "Updates a new Vacations.", 
        security = {
            @SecurityRequirement(
                name = OpenApiConfig.OAUTH2,
                scopes = {Scopes.VACATIONS_WRITE, Scopes.VACATIONS_ALL_WRITE}
            )
        }, 
        tags = {"Vacations"}, 
        responses = {
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = VacationUpdatedResponse.class))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(oneOf = {BadRequestResponse.class, InvalidCaptchaResponse.class}))),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = UnauthorizedResponse.class))),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = ForbiddenResponse.class))),
            @ApiResponse(description = "Conflict", responseCode = "409", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = ConflictResponse.class))),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = InternalServerErrorResponse.class)))
        }
    )
    // @formatter:on
    public ResponseEntity<GenericResponse<Vacation>> updateByUuid(
    // @formatter:off
        final Authentication authentication,
        @PathVariable("uuid") final UUID uuid,
        @Valid @RequestBody(required = true, content = @Content(schema =  @Schema(implementation = VacationDTO.class))) @org.springframework.web.bind.annotation.RequestBody final VacationDTO vacationsDTO
     // @formatter:on
    ) {
        GenericResponse<Vacation> response = new GenericResponse<>(HttpStatus.NO_CONTENT);

        try {
            Vacation vacation = this.vacationService.update(vacationsDTO.toObject());
            response.setData(vacation);
        } catch (VacationNotFoundException e) {
            log.info("Vacation could not be found.");
            return response.fromError(HttpStatus.NOT_FOUND, e.toApiError()).build();
        }

        return response.build();
    }

    /**
     * Index route for /vacations endpoint. Updates an existing Vacations.
     * (if user is authorized).
     * 
     * @since 1.0.0
     * @param authentication Will be provided by Spring Security.
     * @param vacationDTO    Vacation object.
     */
    @DeleteMapping(value = "/{uuid}")
    // @RequiresCaptcha
    // @formatter:off
    @Operation(
        summary = "Deletes Vacations.", 
        description = "Deletes Vacations with given uuid.", 
        security = {
            @SecurityRequirement(
                name = OpenApiConfig.OAUTH2,
                scopes = {Scopes.VACATIONS_WRITE, Scopes.VACATIONS_ALL_WRITE}
            )
        }, 
        tags = {"Vacations"}, 
        responses = {
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = VacationUpdatedResponse.class))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(oneOf = {BadRequestResponse.class, InvalidCaptchaResponse.class}))),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = UnauthorizedResponse.class))),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = ForbiddenResponse.class))),
            @ApiResponse(description = "Conflict", responseCode = "409", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = ConflictResponse.class))),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = InternalServerErrorResponse.class)))
        }
    )
    // @formatter:on
    public ResponseEntity<GenericResponse<Vacation>> deleteByUuid(
    // @formatter:off
        final Authentication authentication,
        @PathVariable("uuid") final UUID uuid,
        @Valid @RequestBody(required = true, content = @Content(schema =  @Schema(implementation = VacationDTO.class))) @org.springframework.web.bind.annotation.RequestBody final VacationDTO vacationDTO
     // @formatter:on
    ) {
        GenericResponse<Vacation> response = new GenericResponse<>(HttpStatus.NO_CONTENT);

        try {
            this.vacationService.deleteByUuid(uuid);
        } catch (VacationNotFoundException e) {
            log.info("Vacation could not be found.");
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
    private final class VacationListResponse extends GenericResponse<List<Vacation>> {

        /**
         * Default Constructor required by Java.
         * 
         * @param status HTTP Status.
         */
        public VacationListResponse(final HttpStatus status) {
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
    private final class VacationResponse extends GenericResponse<Vacation> {

        /**
         * Default Constructor required by Java.
         * 
         * @param status HTTP Status.
         */
        public VacationResponse(final HttpStatus status) {
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
    private final class VacationCreatedResponse extends CreatedResponse<Vacation> {
    };

    /**
     * Helper class for OpenAPI generation.
     * 
     * @author Michael Beutler
     * @version 1.0.0
     * @since 1.0.0
     */
    private final class VacationUpdatedResponse extends UpdatedResponse<Vacation> {
    };

    private final class VacationDaysCountByMonthResponse extends GenericResponse<double[]> {
        public VacationDaysCountByMonthResponse(final HttpStatus status) {
            super(status);
        }
    };
}