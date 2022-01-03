package com.iperka.vacations.api.vacations;

import java.time.Year;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import com.iperka.vacations.api.OpenApiConfig;
import com.iperka.vacations.api.helpers.Response;
import com.iperka.vacations.api.security.Helpers;
import com.iperka.vacations.api.security.Scopes;
import com.iperka.vacations.api.vacations.dto.VacationDTO;
import com.iperka.vacations.api.vacations.exceptions.VacationInvalidDateRange;
import com.iperka.vacations.api.vacations.exceptions.VacationNotFound;

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
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

/**
 * The {@link com.iperka.vacations.api.vacations.VacationController}
 * class defines the structure of a basic vacation route.
 * 
 * @author Michael Beutler
 * @version 0.0.5
 * @since 2021-12-28
 */
@RestController
@RequestMapping(path = { "/vacations" }, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Vacations", description = "Endpoints for managing vacations.")
public class VacationController {

    private final VacationService vacationService;

    @Autowired
    public VacationController(final VacationService vacationService) {
        this.vacationService = vacationService;
    }

    @GetMapping
    // @formatter:off
    @Operation(
        summary = "Finds all vacations", 
        description = "Finds all vacations owned by authenticated user.", 
        security = {
            @SecurityRequirement(
                name = OpenApiConfig.OAUTH2,
                scopes = {Scopes.VACATIONS_READ, Scopes.VACATIONS_WRITE, Scopes.VACATIONS_ALL_READ, Scopes.VACATIONS_ALL_WRITE}
            )
        }, 
        tags = {"Vacations"}, 
        responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = VacationsListResponse.class))),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON)),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON)),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON))
        }
    )
    // @formatter:on
    public ResponseEntity<Response<List<Vacation>>> getAllVacations(
    // @formatter:off
        final Authentication authentication,
        @ParameterObject @PageableDefault(size = 20, sort = "name") final Pageable pageable,
        @RequestParam(required = false) @Parameter(description = "Filter owned vacations by name.") final String name,
        @RequestParam(required = false) @Parameter(description = "Filter vacations by owner (advanced scopes required).") final String owner
     // @formatter:on
    ) {
        final String userId = Helpers.getUserId(authentication);
        String query = null;

        // Check if name and / or owner is set
        if (StringUtils.hasText(owner)) {
            query = String.format("owner=*%s*", owner);
        } else if (StringUtils.hasText(name)) {
            query = String.format("name=*%s*", name);
        } else if (StringUtils.hasText(name) && StringUtils.hasText(owner)) {
            query = String.format("name=*%s*,owner=%s", name, owner);
        }

        Page<Vacation> page;
        // Check if authenticated user has been granted vacations:all:read
        if (Helpers.hasScope(Scopes.VACATIONS_ALL_READ, authentication) && !StringUtils.hasText(owner)) {
            if (StringUtils.hasText(name)) {
                page = this.vacationService.findByNameContainingIgnoreCase(pageable, name);
            } else {
                page = this.vacationService.findAll(pageable);
            }
        } else {
            if (StringUtils.hasText(name)) {
                page = this.vacationService.findByNameContainingIgnoreCaseAndOwner(pageable, name, userId);
            } else {
                page = this.vacationService.findAllByOwner(pageable, userId);
            }
        }

        return Response.fromPage(HttpStatus.OK, page, query).build();
    }

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
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON)),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON)),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON))
        }
    )
    // @formatter:on
    public ResponseEntity<Response<double[]>> getVacationCountDaysByMonth(
    // @formatter:off
        final Authentication authentication,
        @PathVariable("year") @Parameter(description = "Will only display vacations within the given year.", example = "2022", schema = @Schema(implementation = int.class)) final Year year,
        @RequestParam(required = false) @Parameter(description = "Filter vacations by owner (advanced scopes required).") final String owner
     // @formatter:on
    ) {
        final String userId = Helpers.getUserId(authentication);
        final Response<double[]> response = new Response<>(HttpStatus.OK);

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

    @GetMapping(value = "/{uuid}")
    // @formatter:off
    @Operation(
        summary = "Finds vacations with given UUID.", 
        description = "Finds vacations with given UUID owned by authenticated user.", 
        security = {
            @SecurityRequirement(
                name = OpenApiConfig.OAUTH2, 
                scopes = {Scopes.VACATIONS_READ, Scopes.VACATIONS_WRITE, Scopes.VACATIONS_ALL_READ, Scopes.VACATIONS_ALL_WRITE}
            )
        }, 
        tags = {"Vacations"}, 
        responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = VacationResponse.class))),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON)),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON)),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON)),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON))
        }
    )
    // @formatter:on
    public ResponseEntity<Response<Vacation>> getVacationById(
    // @formatter:off
        final Authentication authentication,
        @PathVariable("uuid") final UUID uuid
    // @formatter:on
    ) {
        final String userId = Helpers.getUserId(authentication);
        final Response<Vacation> response = new Response<>(HttpStatus.OK);

        try {
            Vacation vacation;

            // Check if authenticated user has been granted vacations:all:read
            if (Helpers.hasScope(Scopes.VACATIONS_ALL_READ, authentication)) {
                vacation = vacationService.findByUuid(uuid);
            } else {
                vacation = vacationService.findByUuidAndOwner(uuid, userId);
            }

            // Set data object
            response.setData(vacation);

            return response.build();
        } catch (final VacationNotFound e) {
            return response.fromError(HttpStatus.NOT_FOUND, e.toApiError()).build();
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    // @formatter:off
    @Operation(
        summary = "Creates a new vacations with given name.", 
        description = "Creates a new vacations with given name and owned by authenticated user.", 
        security = {
            @SecurityRequirement(
                name = OpenApiConfig.OAUTH2, 
                scopes = Scopes.VACATIONS_WRITE
            ) 
        }, 
        tags = {"Vacations"}, 
        responses = {
            @ApiResponse(description = "Created", responseCode = "201", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = VacationResponse.class))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON)),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON)),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON)),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON))
        }
    )
    // @formatter:on
    public ResponseEntity<Response<Vacation>> createOrganisation(
    // @formatter:off    
        final Authentication authentication,
        @Valid @RequestBody(required = true, content = @Content(schema =  @Schema(implementation = VacationDTO.class))) @org.springframework.web.bind.annotation.RequestBody final VacationDTO vacationDTO
    // @formatter:on
    ) {
        final Vacation vacation = vacationDTO.toObject();
        vacation.setOwner(Helpers.getUserId(authentication));

        if (!Helpers.hasScope(Scopes.VACATIONS_ALL_WRITE, authentication)) {
            vacationDTO.setStatus(VacationStatus.REQUESTED.toString());
        }

        final Response<Vacation> response = new Response<>(HttpStatus.CREATED);
        try {
            response.setData(this.vacationService.create(vacation));
        } catch (final VacationInvalidDateRange e) {
            return response.fromError(HttpStatus.BAD_REQUEST, e.toApiError()).build();
        }

        return response.build();
    }

    @DeleteMapping(value = "/{uuid}")
    // @formatter:off
    @Operation(
        summary = "Deletes vacation with given UUID.", 
        description = "Deletes vacations with given UUID and owned by authenticated user.", 
        security = {
            @SecurityRequirement(
                name = OpenApiConfig.OAUTH2, 
                scopes = {Scopes.VACATIONS_WRITE, Scopes.VACATIONS_ALL_WRITE}
            ) 
        }, 
        tags = {"Vacations"},
        responses = {
            @ApiResponse(description = "OK", responseCode = "200", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = Response.class))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON)),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON)),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON)),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON))
        }
    )
    // @formatter:on
    public ResponseEntity<Response<Vacation>> deleteVacationById(
    // @formatter:off
        final Authentication authentication,
        @PathVariable("uuid") final UUID uuid
    // @formatter:on        
    ) {
        final String userId = Helpers.getUserId(authentication);
        final Response<Vacation> response = new Response<>(HttpStatus.OK);

        try {
            Vacation vacation;

            // Check if authenticated user has been granted vacations:all:write
            if (Helpers.hasScope(Scopes.VACATIONS_ALL_WRITE, authentication)) {
                vacation = vacationService.findByUuid(uuid);
            } else {
                vacation = vacationService.findByUuidAndOwner(uuid, userId);
            }

            // Delete
            vacationService.deleteByUuidAndOwner(uuid, userId);

            // Set data object
            response.setData(vacation);

            return response.build();
        } catch (final VacationNotFound e) {
            return response.fromError(HttpStatus.NOT_FOUND, e.toApiError()).build();
        }
    }

    @PutMapping(value = "/{uuid}")
    @PatchMapping(value = "/{uuid}")
    // @formatter:off
    @Operation(
        summary = "Updates vacation with given UUID.", 
        description = "Updates vacations with given UUID and vacation object and owned by authenticated user.", 
        security = {
            @SecurityRequirement(
                name = OpenApiConfig.OAUTH2, 
                scopes = {Scopes.VACATIONS_WRITE, Scopes.VACATIONS_ALL_WRITE}
            ) 
        }, 
        tags = {"Vacations"},
        responses = {
            @ApiResponse(description = "OK", responseCode = "200", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = VacationResponse.class))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON)),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON)),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON)),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON))
        }
    )
    // @formatter:on
    public ResponseEntity<Response<Vacation>> updateVacationById(
    // @formatter:off
        final Authentication authentication,
        @PathVariable("uuid") final UUID uuid,
        @Valid @RequestBody(required = true, content = @Content(schema =  @Schema(implementation = VacationDTO.class))) @org.springframework.web.bind.annotation.RequestBody final VacationDTO vacationDTO
    // @formatter:on        
    ) {
        final String userId = Helpers.getUserId(authentication);
        final Response<Vacation> response = new Response<>(HttpStatus.OK);

        try {
            Vacation vacation;

            // Check if authenticated user has been granted vacations:all:write
            if (Helpers.hasScope(Scopes.VACATIONS_ALL_WRITE, authentication)) {
                vacation = vacationService.updateByUuid(uuid, vacationDTO);
            } else {
                vacation = vacationService.updateByUuidAndOwner(uuid, userId, vacationDTO);
            }

            // Set data object
            response.setData(vacation);

            return response.build();
        } catch (final VacationNotFound e) {
            return response.fromError(HttpStatus.NOT_FOUND, e.toApiError()).build();
        } catch (final VacationInvalidDateRange e) {
            return response.fromError(HttpStatus.BAD_REQUEST, e.toApiError()).build();
        }
    }

    private final class VacationsListResponse extends Response<List<Vacation>> {
        public VacationsListResponse(final HttpStatus status) {
            super(status);
        }
    };

    private final class VacationResponse extends Response<Vacation> {
        public VacationResponse(final HttpStatus status) {
            super(status);
        }
    };

    private final class VacationDaysCountByMonthResponse extends Response<double[]> {
        public VacationDaysCountByMonthResponse(final HttpStatus status) {
            super(status);
        }

        @Schema(description = "Each index represents a month.", required = true, example = "[0.0,10.0,0.0,0.0,4.0,0.0,0.0,0.1,0.0,0.0,0.0,0.0]")
        public double[] getData() {
            return new double[12];
        }
    };
}
