package com.iperka.vacations.api.users;

import com.auth0.json.mgmt.users.User;
import com.iperka.vacations.api.OpenApiConfig;
import com.iperka.vacations.api.helpers.Response;
import com.iperka.vacations.api.security.Scopes;
import com.iperka.vacations.api.users.auth0.ManagementService;
import com.iperka.vacations.api.users.auth0.exceptions.NotConfigured;
import com.iperka.vacations.api.users.exceptions.UserNotFound;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * The {@link com.iperka.vacations.api.users.UserController}
 * class defines the structure of a basic users route.
 * 
 * @author Michael Beutler
 * @version 0.0.1
 * @since 2021-12-31
 */
@RestController
@RequestMapping(path = { "/users" }, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Users", description = "Endpoints for managing users.")
public class UserController {
    private final ManagementService managementService;

    @Autowired
    public UserController(final ManagementService managementService) {
        this.managementService = managementService;
    }

    @GetMapping(value = "/{userId}")
    // @formatter:off
    @Operation(
        summary = "Finds user with given id.", 
        security = {
            @SecurityRequirement(
                name = OpenApiConfig.OAUTH2, 
                scopes = {Scopes.USERS_ALL_READ, Scopes.USERS_ALL_WRITE}
            )
        }, 
        tags = {"Users"}, 
        responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON)),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON)),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON)),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON))
        }
    )
    // @formatter:on
    public ResponseEntity<Response<User>> getUserById(
    // @formatter:off
        @PathVariable("userId") final String userId
    // @formatter:on
    ) {
        final Response<User> response = new Response<>(HttpStatus.OK);

        try {
            User user = managementService.getUserById(userId).orElseThrow();

            // Set data object
            response.setData(user);

            return response.build();
        } catch (final UserNotFound e) {
            return response.fromError(HttpStatus.NOT_FOUND, e.toApiError()).build();
        } catch (final NotConfigured e) {
            return response.fromError(HttpStatus.NOT_ACCEPTABLE, e.toApiError()).build();
        }
    }

    private final class UserResponse extends Response<User> {
        public UserResponse(final HttpStatus status) {
            super(status);
        }
    };
}
