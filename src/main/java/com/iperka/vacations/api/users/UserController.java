package com.iperka.vacations.api.users;



import javax.validation.Valid;

import com.auth0.json.mgmt.users.User;
import com.iperka.vacations.api.config.OpenApiConfig;
import com.iperka.vacations.api.helpers.GenericResponse;
import com.iperka.vacations.api.helpers.openapi.responses.BadRequestResponse;
import com.iperka.vacations.api.helpers.openapi.responses.ConflictResponse;
import com.iperka.vacations.api.helpers.openapi.responses.CreatedResponse;
import com.iperka.vacations.api.helpers.openapi.responses.ForbiddenResponse;
import com.iperka.vacations.api.helpers.openapi.responses.InternalServerErrorResponse;
import com.iperka.vacations.api.helpers.openapi.responses.UnauthorizedResponse;
import com.iperka.vacations.api.helpers.openapi.responses.UpdatedResponse;
import com.iperka.vacations.api.security.Helpers;
import com.iperka.vacations.api.security.Scopes;
import com.iperka.vacations.api.users.auth0.ManagementService;
import com.iperka.vacations.api.users.auth0.exceptions.NotConfiguredException;
import com.iperka.vacations.api.users.dto.SimpleUserDTO;
import com.iperka.vacations.api.users.dto.UserDTO;
import com.iperka.vacations.api.users.exceptions.EmailAndOrPhoneAlreadyConnectedException;
import com.iperka.vacations.api.users.exceptions.UserNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * The {@link com.iperka.users.api.users.UserController}
 * class defines the structure of a basic users route.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 1.0.9
 */
@RestController
@RequestMapping(path = { "/users" }, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Users", description = "Endpoints for managing users.")
public class UserController {
    @Autowired
    private ManagementService managementService;

    @Autowired
    private UserService userService;

    @GetMapping(value = "/{userId}")
    // @formatter:off
    @Operation(
        summary = "Finds user with given id.", 
        security = {
            @SecurityRequirement(
                name = OpenApiConfig.OAUTH2, 
                scopes = {Scopes.USERS_READ, Scopes.USERS_WRITE,Scopes.USERS_ALL_READ, Scopes.USERS_ALL_WRITE}
            )
        }, 
        tags = {"Users"}, 
        responses = {
            @ApiResponse(description = "Created", responseCode = "201", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = BadRequestResponse.class))),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = UnauthorizedResponse.class))),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = ForbiddenResponse.class))),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON)),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = InternalServerErrorResponse.class)))
        }
    )
    // @formatter:on
    public ResponseEntity<GenericResponse<SimpleUserDTO>> getUserById(
    // @formatter:off
        @PathVariable("userId") final String userId
    // @formatter:on
    ) {
        final GenericResponse<SimpleUserDTO> response = new GenericResponse<>(HttpStatus.OK);

        try {
            User user = managementService.getUserById(userId.replace('_', '|')).orElseThrow();

            // Set data object
            if (user.getUsername() == null) {
                response.setData(
                        new SimpleUserDTO(user.getId(), user.getName(), user.getEmail().split("@")[0],
                                user.getPicture()));
            } else {
                response.setData(
                        new SimpleUserDTO(user.getId(), user.getName(), user.getUsername(), user.getPicture()));
            }

            return response.build();
        } catch (final UserNotFoundException e) {
            return response.fromError(HttpStatus.NOT_FOUND, e.toApiError()).build();
        } catch (final NotConfiguredException e) {
            return response.fromError(HttpStatus.NOT_ACCEPTABLE, e.toApiError()).build();
        }
    }

    @GetMapping(value = "/email/{emailHash}")
    // @formatter:off
    @Operation(
        summary = "Finds user with given email hash.", 
        security = {
            @SecurityRequirement(
                name = OpenApiConfig.OAUTH2, 
                scopes = {Scopes.USERS_READ, Scopes.USERS_WRITE,Scopes.USERS_ALL_READ, Scopes.USERS_ALL_WRITE}
            )
        }, 
        tags = {"Users"}, 
        responses = {
            @ApiResponse(description = "Created", responseCode = "201", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = BadRequestResponse.class))),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = UnauthorizedResponse.class))),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = ForbiddenResponse.class))),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON)),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = InternalServerErrorResponse.class)))
        }
    )
    // @formatter:on
    public ResponseEntity<GenericResponse<SimpleUserDTO>> getUserByEmailHash(
    // @formatter:off
        @PathVariable("emailHash") final String emailHash
    // @formatter:on
    ) {
        final GenericResponse<SimpleUserDTO> response = new GenericResponse<>(HttpStatus.OK);

        try {
            User user = managementService.getUserById(userService.findByEmailHash(emailHash).getOwner()).orElseThrow();

            // Set data object
            if (user.getUsername() == null) {
                response.setData(
                        new SimpleUserDTO(user.getId(), user.getName(), user.getEmail().split("@")[0],
                                user.getPicture()));
            } else {
                response.setData(
                        new SimpleUserDTO(user.getId(), user.getName(), user.getUsername(), user.getPicture()));
            }

            return response.build();
        } catch (final UserNotFoundException e) {
            return response.fromError(HttpStatus.NOT_FOUND, e.toApiError()).build();
        } catch (final NotConfiguredException e) {
            return response.fromError(HttpStatus.NOT_ACCEPTABLE, e.toApiError()).build();
        }
    }

    @GetMapping(value = "/phone/{phoneHash}")
    // @formatter:off
    @Operation(
        summary = "Finds user with given phone hash.", 
        security = {
            @SecurityRequirement(
                name = OpenApiConfig.OAUTH2, 
                scopes = {Scopes.USERS_READ, Scopes.USERS_WRITE,Scopes.USERS_ALL_READ, Scopes.USERS_ALL_WRITE}
            )
        }, 
        tags = {"Users"}, 
        responses = {
            @ApiResponse(description = "Created", responseCode = "201", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = BadRequestResponse.class))),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = UnauthorizedResponse.class))),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = ForbiddenResponse.class))),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON)),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = InternalServerErrorResponse.class)))
        }
    )
    // @formatter:on
    public ResponseEntity<GenericResponse<SimpleUserDTO>> getUserByPhoneHash(
    // @formatter:off
        @PathVariable("phoneHash") final String phoneHash
    // @formatter:on
    ) {
        final GenericResponse<SimpleUserDTO> response = new GenericResponse<>(HttpStatus.OK);

        try {
            User user = managementService.getUserById(userService.findByPhoneHash(phoneHash).getOwner()).orElseThrow();

            // Set data object
            if (user.getUsername() == null) {
                response.setData(
                        new SimpleUserDTO(user.getId(), user.getName(), user.getEmail().split("@")[0],
                                user.getPicture()));
            } else {
                response.setData(
                        new SimpleUserDTO(user.getId(), user.getName(), user.getUsername(), user.getPicture()));
            }

            return response.build();
        } catch (final UserNotFoundException e) {
            return response.fromError(HttpStatus.NOT_FOUND, e.toApiError()).build();
        } catch (final NotConfiguredException e) {
            return response.fromError(HttpStatus.NOT_ACCEPTABLE, e.toApiError()).build();
        }
    }

    /**
     * Index route for /users endpoint. Creates a new User. (if user is
     * authorized).
     * 
     * @since 1.0.9
     * @param authentication Will be provided by Spring Security.
     * @param userDTO        Vacation object.
     * @return A generic Response with created users as data property.
     */
    @PostMapping
    // @RequiresCaptcha
    // @formatter:off
    @Operation(
        summary = "Creates a User.", 
        // TODO: Extend description.
        description = "Creates a new User.", 
        security = {
            @SecurityRequirement(
                name = OpenApiConfig.OAUTH2,
                scopes = {Scopes.USERS_WRITE, Scopes.USERS_ALL_WRITE}
            )
        }, 
        tags = {"Vacations"}, 
        responses = {
            @ApiResponse(description = "Created", responseCode = "201", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = UserCreatedResponse.class))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = BadRequestResponse.class))),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = UnauthorizedResponse.class))),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = ForbiddenResponse.class))),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON)),
            @ApiResponse(description = "Conflict", responseCode = "409", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = ConflictResponse.class))),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = InternalServerErrorResponse.class)))
        }
    )
    // @formatter:on
    public ResponseEntity<GenericResponse<UserDTO>> create(
    // @formatter:off
        final Authentication authentication,
        @Valid @RequestBody(required = true, content = @Content(schema =  @Schema(implementation = UserDTO.class))) @org.springframework.web.bind.annotation.RequestBody final UserDTO userDTO
     // @formatter:on
    ) {
        GenericResponse<UserDTO> response = new GenericResponse<>(HttpStatus.CREATED);

        com.iperka.vacations.api.users.User user = userDTO.toObject();
        user.setOwner(Helpers.getUserId(authentication));
        try {
            user = this.userService.create(user);
        } catch (EmailAndOrPhoneAlreadyConnectedException e) {
            return response.fromError(HttpStatus.CONFLICT, e.toApiError()).build();
        }
        response.setData(userDTO);

        return response.build();
    }

    /**
     * Index route for /users endpoint. Deletes an existing user.
     * (if user is authorized).
     * 
     * @since 1.0.9
     * @param authentication Will be provided by Spring Security.
     * @param vacationDTO    Vacation object.
     */
    @DeleteMapping(value = "/{id}")
    // @RequiresCaptcha
    // @formatter:off
    @Operation(
        summary = "Deletes User.", 
        description = "Deletes User with given id.", 
        security = {
            @SecurityRequirement(
                name = OpenApiConfig.OAUTH2,
                scopes = {Scopes.USERS_WRITE, Scopes.USERS_ALL_WRITE}
            )
        }, 
        tags = {"Users"}, 
        responses = {
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = UserUpdatedResponse.class))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = BadRequestResponse.class))),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = UnauthorizedResponse.class))),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = ForbiddenResponse.class))),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON)),
            @ApiResponse(description = "Conflict", responseCode = "409", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = ConflictResponse.class))),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = InternalServerErrorResponse.class)))
        }
    )
    // @formatter:on
    public ResponseEntity<GenericResponse<SimpleUserDTO>> deleteById(
    // @formatter:off
        final Authentication authentication,
        @PathVariable("id") final String id
     // @formatter:on
    ) {
        final String userId = Helpers.getUserId(authentication);
        GenericResponse<SimpleUserDTO> response = new GenericResponse<>(HttpStatus.NO_CONTENT);

        try {
            if (Helpers.hasScope(Scopes.USERS_ALL_WRITE, authentication)) {
                this.userService.deleteById(id);
            } else {
                this.userService.deleteByIdAndOwner(id, userId);
            }
        } catch (UserNotFoundException e) {
            return response.fromError(HttpStatus.NOT_FOUND, e.toApiError()).build();
        }

        return response.build();
    }

    /**
     * Helper class for OpenAPI generation.
     * 
     * @author Michael Beutler
     * @version 1.0.0
     * @since 1.0.9
     */
    private final class UserCreatedResponse extends CreatedResponse<SimpleUserDTO> {
    };

    /**
     * Helper class for OpenAPI generation.
     * 
     * @author Michael Beutler
     * @version 1.0.0
     * @since 1.0.9
     */
    private final class UserUpdatedResponse extends UpdatedResponse<SimpleUserDTO> {
    };

    /**
     * Helper class for OpenAPI generation.
     * 
     * @author Michael Beutler
     * @version 1.0.0
     * @since 1.0.9
     */
    private final class UserResponse extends GenericResponse<SimpleUserDTO> {

        /**
         * Default Constructor required by Java.
         * 
         * @param status HTTP Status.
         */
        public UserResponse(final HttpStatus status) {
            super(status);
        }
    };
}
