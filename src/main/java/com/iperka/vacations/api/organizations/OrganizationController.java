package com.iperka.vacations.api.organizations;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import com.iperka.vacations.api.OpenApiConfig;
import com.iperka.vacations.api.helpers.Response;
import com.iperka.vacations.api.organizations.dto.OrganizationDTO;
import com.iperka.vacations.api.organizations.exceptions.OrganizationAlreadyExists;
import com.iperka.vacations.api.organizations.exceptions.OrganizationNotFound;
import com.iperka.vacations.api.security.Helpers;
import com.iperka.vacations.api.security.Scopes;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * The {@link com.iperka.vacations.api.organizations.OrganizationController}
 * class defines the structure of a basic organization route.
 * 
 * @author Michael Beutler
 * @version 0.1.1
 * @since 2021-09-29
 */
@RestController
@RequestMapping(path = { "/organizations" }, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Organizations", description = "Endpoints for managing organizations.")
public class OrganizationController {

    private final OrganizationService organizationService;

    @Autowired
    public OrganizationController(final OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping
    // @formatter:off
    @Operation(
        summary = "Finds all organizations", 
        description = "Finds all organizations owned by authenticated user.", 
        security = {
            @SecurityRequirement(
                name = OpenApiConfig.OAUTH2,
                scopes = {Scopes.ORGANIZATIONS_READ, Scopes.ORGANIZATIONS_WRITE, Scopes.ORGANIZATIONS_ALL_READ, Scopes.ORGANIZATIONS_ALL_WRITE}
            )
        }, 
        tags = {"Organizations"}, 
        responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = OrganizationsListResponse.class))),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON)),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON)),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON))
        }
    )
    // @formatter:on
    public ResponseEntity<Response<List<Organization>>> getAllOrganizations(
    // @formatter:off
        final Authentication authentication,
        @ParameterObject @PageableDefault(size = 20, sort = "name") final Pageable pageable,
        @RequestParam(required = false) @Parameter(description = "Filter owned organizations by name.") final String name,
        @RequestParam(required = false) @Parameter(description = "Filter organizations by owner (advanced scopes required).") final String owner
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

        Page<Organization> page;
        // Check if authenticated user has been granted organizations:all:read
        if (Helpers.hasScope(Scopes.ORGANIZATIONS_ALL_READ, authentication) && !StringUtils.hasText(owner)) {
            if (StringUtils.hasText(name)) {
                page = this.organizationService.findByNameContainingIgnoreCase(pageable, name);
            } else {
                page = this.organizationService.findAll(pageable);
            }
        } else {
            if (StringUtils.hasText(name)) {
                page = this.organizationService.findByNameContainingIgnoreCaseAndOwner(pageable, name, userId);
            } else {
                page = this.organizationService.findAllByOwner(pageable, userId);
            }
        }

        return Response.fromPage(HttpStatus.OK, page, query).build();
    }

    @GetMapping(value = "/{uuid}")
    // @formatter:off
    @Operation(
        summary = "Finds organizations with given UUID.", 
        description = "Finds organizations with given UUID owned by authenticated user.", 
        security = {
            @SecurityRequirement(
                name = OpenApiConfig.OAUTH2, 
                scopes = {Scopes.ORGANIZATIONS_READ, Scopes.ORGANIZATIONS_WRITE, Scopes.ORGANIZATIONS_ALL_READ, Scopes.ORGANIZATIONS_ALL_WRITE}
            )
        }, 
        tags = {"Organizations"}, 
        responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = OrganizationResponse.class))),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON)),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON)),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON)),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON))
        }
    )
    // @formatter:on
    public ResponseEntity<Response<Organization>> getOrganizationById(
    // @formatter:off
        final Authentication authentication,
        @PathVariable("uuid") final UUID uuid
    // @formatter:on
    ) {
        final String userId = Helpers.getUserId(authentication);
        final Response<Organization> response = new Response<>(HttpStatus.OK);

        try {
            Organization organization;

            // Check if authenticated user has been granted organizations:all:read
            if (Helpers.hasScope(Scopes.ORGANIZATIONS_ALL_READ, authentication)) {
                organization = organizationService.findByUuid(uuid);
            } else {
                organization = organizationService.findByUuidAndOwner(uuid, userId);
            }

            // Set data object
            response.setData(organization);

            return response.build();
        } catch (final OrganizationNotFound e) {
            return response.fromError(HttpStatus.NOT_FOUND, e.toApiError()).build();
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    // @formatter:off
    @Operation(
        summary = "Creates a new organizations with given name.", 
        description = "Creates a new organizations with given name and owned by authenticated user.", 
        security = {
            @SecurityRequirement(
                name = OpenApiConfig.OAUTH2, 
                scopes = Scopes.ORGANIZATIONS_WRITE
            ) 
        }, 
        tags = {"Organizations"}, 
        responses = {
            @ApiResponse(description = "Created", responseCode = "201", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = OrganizationResponse.class))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON)),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON)),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON)),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON))
        }
    )
    // @formatter:on
    public ResponseEntity<Response<Organization>> createOrganisation(
    // @formatter:off    
        final Authentication authentication,
        @Valid @RequestBody(required = true) @Schema(implementation = OrganizationDTO.class) final OrganizationDTO organizationDTO
    // @formatter:on
    ) {
        final Organization organization = organizationDTO.toObject();
        organization.setOwner(((Jwt) authentication.getPrincipal()).getSubject());

        final Response<Organization> response = new Response<>(HttpStatus.CREATED);
        try {
            response.setData(this.organizationService.create(organization));
        } catch (final OrganizationAlreadyExists e) {
            return response.fromError(HttpStatus.BAD_REQUEST, e.toApiError()).build();
        }

        return response.build();
    }

    @DeleteMapping(value = "/{uuid}")
    // @formatter:off
    @Operation(
        summary = "Deletes organization with given UUID.", 
        description = "Deletes organizations with given UUID and owned by authenticated user.", 
        security = {
            @SecurityRequirement(
                name = OpenApiConfig.OAUTH2, 
                scopes = {Scopes.ORGANIZATIONS_WRITE, Scopes.ORGANIZATIONS_ALL_WRITE}
            ) 
        }, 
        tags = {"Organizations"},
        responses = {
            @ApiResponse(description = "OK", responseCode = "200", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = Response.class))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON)),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON)),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON)),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON))
        }
    )
    // @formatter:on
    public ResponseEntity<Response<Organization>> deleteOrganizationById(
    // @formatter:off
        final Authentication authentication,
        @PathVariable("uuid") final UUID uuid
    // @formatter:on        
    ) {
        final String userId = Helpers.getUserId(authentication);
        final Response<Organization> response = new Response<>(HttpStatus.OK);

        try {
            Organization organization;

            // Check if authenticated user has been granted organizations:all:write
            if (Helpers.hasScope(Scopes.ORGANIZATIONS_ALL_WRITE, authentication)) {
                organization = organizationService.findByUuid(uuid);
            } else {
                organization = organizationService.findByUuidAndOwner(uuid, userId);
            }

            // Delete
            organizationService.deleteByUuidAndOwner(uuid, userId);

            // Set data object
            response.setData(organization);

            return response.build();
        } catch (final OrganizationNotFound e) {
            return response.fromError(HttpStatus.NOT_FOUND, e.toApiError()).build();
        }
    }

    @PutMapping(value = "/{uuid}")
    @PatchMapping(value = "/{uuid}")
    // @formatter:off
    @Operation(
        summary = "Updates organization with given UUID.", 
        description = "Updates organizations with given UUID and organization object and owned by authenticated user.", 
        security = {
            @SecurityRequirement(
                name = OpenApiConfig.OAUTH2, 
                scopes = {Scopes.ORGANIZATIONS_WRITE, Scopes.ORGANIZATIONS_ALL_WRITE}
            ) 
        }, 
        tags = {"Organizations"},
        responses = {
            @ApiResponse(description = "OK", responseCode = "200", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = OrganizationResponse.class))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON)),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON)),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON)),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON))
        }
    )
    // @formatter:on
    public ResponseEntity<Response<Organization>> updateOrganizationById(
    // @formatter:off
        final Authentication authentication,
        @PathVariable("uuid") final UUID uuid,
        @Valid @RequestBody(required = true) @Schema(implementation = OrganizationDTO.class) final OrganizationDTO organizationDTO
    // @formatter:on        
    ) {
        final String userId = Helpers.getUserId(authentication);
        final Response<Organization> response = new Response<>(HttpStatus.OK);

        try {
            Organization organization;

            // Check if authenticated user has been granted organizations:all:write
            if (Helpers.hasScope(Scopes.ORGANIZATIONS_ALL_WRITE, authentication)) {
                organization = organizationService.updateByUuid(uuid, organizationDTO);
            } else {
                organization = organizationService.updateByUuidAndOwner(uuid, userId, organizationDTO);
            }

            // Set data object
            response.setData(organization);

            return response.build();
        } catch (final OrganizationNotFound e) {
            return response.fromError(HttpStatus.NOT_FOUND, e.toApiError()).build();
        } catch (final OrganizationAlreadyExists e) {
            return response.fromError(HttpStatus.BAD_REQUEST, e.toApiError()).build();
        }
    }

    private final class OrganizationsListResponse extends Response<List<Organization>> {
        public OrganizationsListResponse(final HttpStatus status) {
            super(status);
        }
    };

    private final class OrganizationResponse extends Response<Organization> {
        public OrganizationResponse(final HttpStatus status) {
            super(status);
        }
    };
}
