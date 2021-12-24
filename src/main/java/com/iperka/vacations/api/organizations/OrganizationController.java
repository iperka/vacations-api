package com.iperka.vacations.api.organizations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import com.iperka.vacations.api.helpers.Response;
import com.iperka.vacations.api.organizations.dto.OrganizationDTO;
import com.iperka.vacations.api.security.Helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
 * REST Controller for Organizations domain.
 */
@RestController
@RequestMapping(path = { "/organizations" }, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Organizations", description = "Endpoints for managing organizations.")
public class OrganizationController {
    private final Logger logger = LoggerFactory.getLogger(OrganizationController.class);

    private final OrganizationService organizationService;

    @Autowired
    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping
    @Operation(summary = "Finds all organizations", description = "Finds all organizations owned by authenticated user.", security = {
            @SecurityRequirement(name = "OAuth2", scopes = { "organizations:read", "organizations:write",
                    "organizations:all:read", "organizations:all:write" }) }, tags = {
                            "Organizations" }, responses = {
                                    @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrganizationsListResponse.class))),
                                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = "application/json")),
                                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = "application/json")),
                                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content(mediaType = "application/json"))
                            })
    public ResponseEntity<Response<List<Organization>>> getAllOrganizations(
            Authentication authentication,
            @ParameterObject @PageableDefault(size = 20, sort = "name") Pageable pageable,
            @RequestParam(required = false) @Parameter(description = "Filter owned organizations by name.") String name) {
        // Check if authenticated user has been granted organizations:all:read
        String owner = Helpers.getOwner(authentication);
        if (Helpers.hasScope("organizations:all:read", authentication)) {
            if (StringUtils.hasText(name)) {
                return Response
                        .fromPage(HttpStatus.OK,
                                this.organizationService.findByNameContainingIgnoreCase(pageable, name),
                                String.format("name=*%s*", name))
                        .build();
            }

            return Response.fromPage(HttpStatus.OK, this.organizationService.findAll(pageable)).build();
        }
        if (StringUtils.hasText(name)) {
            return Response
                    .fromPage(HttpStatus.OK,
                            this.organizationService.findByNameContainingIgnoreCaseAndOwner(pageable, name, owner),
                            String.format("name=*%s*", name))
                    .build();
        }

        return Response.fromPage(HttpStatus.OK, this.organizationService.findAllByOwner(pageable, owner)).build();
    }

    @GetMapping(value = "/{uuid}")
    @Operation(summary = "Finds organizations with given UUID.", description = "Finds organizations with given UUID owned by authenticated user.", security = {
            @SecurityRequirement(name = "OAuth2", scopes = { "organizations:read", "organizations:write",
                    "organizations:all:read", "organizations:all:write" }) }, tags = {
                            "Organizations" }, responses = {
                                    @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrganizationResponse.class))),
                                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content(mediaType = "application/json")),
                                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = "application/json")),
                                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = "application/json")),
                                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content(mediaType = "application/json"))
                            })
    public ResponseEntity<Response<Organization>> getOrganizationById(Authentication authentication,
            @PathVariable("uuid") UUID uuid) {
        // Check if authenticated user has been granted organizations:all:read
        String owner = Helpers.getOwner(authentication);
        if (Helpers.hasScope("organizations:all:read", authentication)) {
            Optional<Organization> optional = organizationService.findByUuid(uuid);

            if (!optional.isPresent()) {
                return Response.<Organization>notFound("No organization found with given id.").build();
            }

            Response<Organization> response = new Response<>(HttpStatus.OK);
            response.setData(optional.get());

            return response.build();
        }
        Optional<Organization> optional = organizationService.findByUuidAndOwner(uuid, owner);

        if (!optional.isPresent()) {
            return Response.<Organization>notFound("No organization found with given id.").build();
        }

        Response<Organization> response = new Response<>(HttpStatus.OK);
        response.setData(optional.get());

        return response.build();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Creates a new organizations with given name.", description = "Creates a new organizations with given name and owned by authenticated user.", security = {
            @SecurityRequirement(name = "OAuth2", scopes = "organizations:write") }, tags = {
                    "Organizations" }, responses = {
                            @ApiResponse(description = "Created", responseCode = "201", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrganizationResponse.class))),
                            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(mediaType = "application/json")),
                            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = "application/json")),
                            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = "application/json")),
                            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content(mediaType = "application/json"))
                    })
    public ResponseEntity<Response<Organization>> createOrganisation(Authentication authentication,
            @Valid @RequestBody(required = true) @Schema(implementation = OrganizationDTO.class) OrganizationDTO organizationDTO) {
        Organization organization = organizationDTO.toObject();
        organization.setOwner(((Jwt) authentication.getPrincipal()).getSubject());

        Response<Organization> response = new Response<Organization>(HttpStatus.CREATED);
        response.setData(this.organizationService.create(organization));

        return response.build();
    }

    @DeleteMapping(value = "/{uuid}")
    @Operation(summary = "Deletes organization with given UUID.", description = "Deletes organizations with given UUID and owned by authenticated user.", security = {
            @SecurityRequirement(name = "OAuth2", scopes = { "organizations:write",
                    "organizations:all:write" }) }, tags = {
                            "Organizations" }, responses = {
                                    @ApiResponse(description = "OK", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))),
                                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(mediaType = "application/json")),
                                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = "application/json")),
                                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = "application/json")),
                                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content(mediaType = "application/json"))
                            })
    public ResponseEntity<Response<Organization>> deleteOrganizationById(Authentication authentication,
            @PathVariable("uuid") UUID uuid) {
        // Check if authenticated user has been granted organizations:all:write
        String owner = Helpers.getOwner(authentication);
        if (Helpers.hasScope("organizations:all:write", authentication)) {
            Optional<Organization> optional = organizationService.findByUuid(uuid);

            if (!optional.isPresent()) {
                return Response.<Organization>notFound("No organization found with given id.").build();
            }

            // Delete
            organizationService.deleteByUuid(uuid);

            Response<Organization> response = new Response<>(HttpStatus.OK);
            response.setData(optional.get());

            return response.build();
        }

        Optional<Organization> optional = organizationService.findByUuidAndOwner(uuid, owner);

        if (!optional.isPresent()) {
            return Response.<Organization>notFound("No organization found with given id.").build();
        }

        // Delete
        organizationService.deleteByUuidAndOwner(uuid, owner);

        Response<Organization> response = new Response<>(HttpStatus.OK);
        response.setData(optional.get());

        return response.build();
    }

    private final class OrganizationsListResponse extends Response<List<Organization>> {
        public OrganizationsListResponse(HttpStatus status) {
            super(status);
        }
    };

    private final class OrganizationResponse extends Response<Organization> {
        public OrganizationResponse(HttpStatus status) {
            super(status);
        }
    };
}
