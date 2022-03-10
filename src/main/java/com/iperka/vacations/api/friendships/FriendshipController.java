package com.iperka.vacations.api.friendships;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import com.iperka.vacations.api.config.OpenApiConfig;
import com.iperka.vacations.api.friendships.dto.FriendshipDTO;
import com.iperka.vacations.api.friendships.exceptions.FriendshipNotFoundException;
import com.iperka.vacations.api.friendships.exceptions.FriendshipRelationAlreadyExistsException;
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

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
 * RestController endpoint for /friendships route.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 1.0.5
 */
@Slf4j
@RestController
@Validated
@RequestMapping(value = "/friendships", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Friendships", description = "Endpoints for CRUD operations with friendships.")
public class FriendshipController {

    @Autowired
    private FriendshipService friendshipService;

    // @Autowired
    // private VacationService vacationService;

    /**
     * Index route for /friendships endpoint. Returns all friendships (if user is
     * authorized).
     * 
     * @since 1.0.5
     * @param authentication Will be provided by Spring Security.
     * @param pageable       Adds built in pagination.
     * @return A generic Response with a list of all friendships as data property.
     */
    @GetMapping
    // @formatter:off
    @Operation(
        summary = "Finds all friendships.", 
        // TODO: Extend description.
        description = "Finds all friendships owned by authenticated user.", 
        security = {
            @SecurityRequirement(
                name = OpenApiConfig.OAUTH2,
                scopes = {Scopes.FRIENDSHIPS_READ, Scopes.FRIENDSHIPS_WRITE, Scopes.FRIENDSHIPS_ALL_READ, Scopes.FRIENDSHIPS_ALL_WRITE}
            )
        }, 
        tags = {"Friendships"}, 
        responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = FriendshipListResponse.class))),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = UnauthorizedResponse.class))),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = ForbiddenResponse.class))),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = InternalServerErrorResponse.class)))
        }
    )
    // @formatter:on
    public ResponseEntity<GenericResponse<List<Friendship>>> findAll(
    // @formatter:off
        final Authentication authentication,
        @ParameterObject @PageableDefault(size = 20, sort = "createdAt") final Pageable pageable,
        @RequestParam(required = false) @Parameter(description = "Filter friendships by owner.") final String owner,
        @RequestParam(required = false) @Parameter(description = "Filter friendships by user.") final String user
     // @formatter:on
    ) {
        final String userId = Helpers.getUserId(authentication);
        String query = null;

        // Check query params
        if (StringUtils.hasText(owner) && StringUtils.hasText(user)) {
            query = String.format("owner=*%s*,user=*%s*", owner, user);
        } else if (StringUtils.hasText(owner)) {
            query = String.format("owner=*%s*", owner);
        } else if (StringUtils.hasText(user)) {
            query = String.format("user=*%s*", user);
        }

        Page<Friendship> page;
        // Check if authenticated user has been granted friendships:all:read
        if (Helpers.hasScope(Scopes.FRIENDSHIPS_ALL_READ, authentication) && !StringUtils.hasText(owner)
                && !StringUtils.hasText(user)) {
            page = this.friendshipService.findAll(pageable);
        } else if (Helpers.hasScope(Scopes.FRIENDSHIPS_ALL_READ, authentication) && StringUtils.hasText(owner)
                && StringUtils.hasText(user)) {
            page = this.friendshipService.findAllByOwnerAndUser(pageable, owner, user);
        } else if (Helpers.hasScope(Scopes.FRIENDSHIPS_ALL_READ, authentication) && StringUtils.hasText(owner)
                && !StringUtils.hasText(user)) {
            page = this.friendshipService.findAllByOwner(pageable, owner);
        } else if (Helpers.hasScope(Scopes.FRIENDSHIPS_ALL_READ, authentication) && !StringUtils.hasText(owner)
                && StringUtils.hasText(user)) {
            page = this.friendshipService.findAllByOwnerAndUser(pageable, userId, user);
        } else if (StringUtils.hasText(user)) {
            page = this.friendshipService.findAllByOwnerAndUser(pageable, userId, user);
        } else {
            page = this.friendshipService.findAllByOwner(pageable, userId);
        }

        return GenericResponse.<Friendship>fromPage(HttpStatus.OK, page, query).build();
    }

    /**
     * Index route for /friendships endpoint. Returns Friendships with given uuid
     * (if
     * user
     * is authorized).
     * 
     * @since 1.0.5
     * @param authentication Will be provided by Spring Security.
     * @param uuid           Id of demanded Friendships object.
     * @return A generic Response with Friendships as data property.
     */
    @GetMapping(value = "/{uuid}")
    // @formatter:off
    @Operation(
        summary = "Finds Friendships with given uuid.", 
        // TODO: Extend description.
        description = "Finds Friendships with given uuid.", 
        security = {
            @SecurityRequirement(
                name = OpenApiConfig.OAUTH2,
                scopes = {Scopes.FRIENDSHIPS_READ, Scopes.FRIENDSHIPS_WRITE, Scopes.FRIENDSHIPS_ALL_READ, Scopes.FRIENDSHIPS_ALL_WRITE}
            )
        }, 
        tags = {"Friendships"}, 
        responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = FriendshipResponse.class))),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = UnauthorizedResponse.class))),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = ForbiddenResponse.class))),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON)),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = InternalServerErrorResponse.class)))
        }
    )
    // @formatter:on
    public ResponseEntity<GenericResponse<Friendship>> findByUuid(
    // @formatter:off
        final Authentication authentication,
        @PathVariable("uuid") final UUID uuid
     // @formatter:on
    ) {
        final String userId = Helpers.getUserId(authentication);
        GenericResponse<Friendship> response = new GenericResponse<>(HttpStatus.OK);

        try {
            if (Helpers.hasScope(Scopes.FRIENDSHIPS_ALL_READ, authentication)
                    || Helpers.hasScope(Scopes.FRIENDSHIPS_ALL_WRITE, authentication)) {
                Friendship friendship = this.friendshipService.findByUuid(uuid);
                response.setData(friendship);
            } else {
                Friendship friendship = this.friendshipService.findByUuidAndOwner(uuid, userId);
                response.setData(friendship);
            }
        } catch (FriendshipNotFoundException e) {
            log.info("Friendship could not be found.");
            return response.fromError(HttpStatus.NOT_FOUND, e.toApiError()).build();
        }

        return response.build();
    }

    /**
     * Index route for /friendships endpoint. Creates a new Friendships. (if user is
     * authorized).
     * 
     * @since 1.0.5
     * @param authentication Will be provided by Spring Security.
     * @param friendshipsDTO Friendship object.
     * @return A generic Response with created friendships as data property.
     */
    @PostMapping
    // @RequiresCaptcha
    // @formatter:off
    @Operation(
        summary = "Creates a Friendships.", 
        // TODO: Extend description.
        description = "Creates a new Friendships.", 
        security = {
            @SecurityRequirement(
                name = OpenApiConfig.OAUTH2,
                scopes = {Scopes.FRIENDSHIPS_WRITE, Scopes.FRIENDSHIPS_ALL_WRITE}
            )
        }, 
        tags = {"Friendships"}, 
        responses = {
            @ApiResponse(description = "Created", responseCode = "201", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = FriendshipCreatedResponse.class))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(oneOf = {BadRequestResponse.class, InvalidCaptchaResponse.class}))),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = UnauthorizedResponse.class))),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = ForbiddenResponse.class))),
            @ApiResponse(description = "Conflict", responseCode = "409", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = ConflictResponse.class))),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = InternalServerErrorResponse.class)))
        }
    )
    // @formatter:on
    public ResponseEntity<GenericResponse<Friendship>> create(
    // @formatter:off
        final Authentication authentication,
        @Valid @RequestBody(required = true, content = @Content(schema =  @Schema(implementation = FriendshipDTO.class))) @org.springframework.web.bind.annotation.RequestBody final FriendshipDTO friendshipsDTO
     // @formatter:on
    ) {
        GenericResponse<Friendship> response = new GenericResponse<>(HttpStatus.CREATED);

        Friendship friendship = friendshipsDTO.toObject();
        friendship.setOwner(Helpers.getUserId(authentication));

        try {
            friendship = this.friendshipService.create(friendship);
        } catch (FriendshipRelationAlreadyExistsException e) {
            return response.fromError(HttpStatus.CONFLICT, e.toApiError()).build();
        }
        response.setData(friendship);

        return response.build();
    }

    /**
     * Index route for /friendships endpoint. Updates an existing Friendships.
     * (if user is authorized).
     * 
     * @since 1.0.5
     * @param authentication Will be provided by Spring Security.
     * @param friendshipsDTO Friendship object.
     * @return A generic Response with created friendships as data property.
     */
    @PutMapping(value = "/{uuid}")
    // @RequiresCaptcha
    // @formatter:off
    @Operation(
        summary = "Updates a Friendships.", 
        // TODO: Extend description.
        description = "Updates a new Friendships.", 
        security = {
            @SecurityRequirement(
                name = OpenApiConfig.OAUTH2,
                scopes = {Scopes.FRIENDSHIPS_WRITE, Scopes.FRIENDSHIPS_ALL_WRITE}
            )
        }, 
        tags = {"Friendships"}, 
        responses = {
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = FriendshipUpdatedResponse.class))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(oneOf = {BadRequestResponse.class, InvalidCaptchaResponse.class}))),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = UnauthorizedResponse.class))),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = ForbiddenResponse.class))),
            @ApiResponse(description = "Conflict", responseCode = "409", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = ConflictResponse.class))),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = InternalServerErrorResponse.class)))
        }
    )
    // @formatter:on
    public ResponseEntity<GenericResponse<Friendship>> updateByUuid(
    // @formatter:off
        final Authentication authentication,
        @PathVariable("uuid") final UUID uuid,
        @Valid @RequestBody(required = true, content = @Content(schema =  @Schema(implementation = FriendshipDTO.class))) @org.springframework.web.bind.annotation.RequestBody final FriendshipDTO friendshipsDTO
     // @formatter:on
    ) {
        final String userId = Helpers.getUserId(authentication);
        GenericResponse<Friendship> response = new GenericResponse<>(HttpStatus.NO_CONTENT);

        try {
            if (Helpers.hasScope(Scopes.FRIENDSHIPS_ALL_WRITE, authentication)) {
                response.setData(this.friendshipService.update(friendshipsDTO.toObject()));
            } else {
                response.setData(this.friendshipService.updateByOwner(friendshipsDTO.toObject(), userId));
            }
        } catch (FriendshipNotFoundException e) {
            log.info("Friendship could not be found.");
            return response.fromError(HttpStatus.NOT_FOUND, e.toApiError()).build();
        }

        return response.build();
    }

    /**
     * Index route for /friendships endpoint. Updates an existing Friendships.
     * (if user is authorized).
     * 
     * @since 1.0.5
     * @param authentication Will be provided by Spring Security.
     * @param friendshipDTO  Friendship object.
     */
    @DeleteMapping(value = "/{uuid}")
    // @RequiresCaptcha
    // @formatter:off
    @Operation(
        summary = "Deletes Friendships.", 
        description = "Deletes Friendships with given uuid.", 
        security = {
            @SecurityRequirement(
                name = OpenApiConfig.OAUTH2,
                scopes = {Scopes.FRIENDSHIPS_WRITE, Scopes.FRIENDSHIPS_ALL_WRITE}
            )
        }, 
        tags = {"Friendships"}, 
        responses = {
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = FriendshipUpdatedResponse.class))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(oneOf = {BadRequestResponse.class, InvalidCaptchaResponse.class}))),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = UnauthorizedResponse.class))),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = ForbiddenResponse.class))),
            @ApiResponse(description = "Conflict", responseCode = "409", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = ConflictResponse.class))),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content(mediaType = OpenApiConfig.APPLICATION_JSON, schema = @Schema(implementation = InternalServerErrorResponse.class)))
        }
    )
    // @formatter:on
    public ResponseEntity<GenericResponse<Friendship>> deleteByUuid(
    // @formatter:off
        final Authentication authentication,
        @PathVariable("uuid") final UUID uuid
     // @formatter:on
    ) {
        final String userId = Helpers.getUserId(authentication);
        GenericResponse<Friendship> response = new GenericResponse<>(HttpStatus.NO_CONTENT);

        try {
            if (Helpers.hasScope(Scopes.FRIENDSHIPS_ALL_WRITE, authentication)) {
                this.friendshipService.deleteByUuid(uuid);
            } else {
                this.friendshipService.deleteByUuidAndOwner(uuid, userId);
            }
        } catch (FriendshipNotFoundException e) {
            log.info("Friendship could not be found.");
            return response.fromError(HttpStatus.NOT_FOUND, e.toApiError()).build();
        }

        return response.build();
    }

    /**
     * Helper class for OpenAPI generation.
     * 
     * @author Michael Beutler
     * @version 1.0.0
     * @since 1.0.5
     */
    private final class FriendshipListResponse extends GenericResponse<List<Friendship>> {
        /**
         * Default Constructor required by Java.
         * 
         * @param status HTTP Status.
         */
        public FriendshipListResponse(final HttpStatus status) {
            super(status);
        }
    };

    /**
     * Helper class for OpenAPI generation.
     * 
     * @author Michael Beutler
     * @version 1.0.0
     * @since 1.0.5
     */
    private final class FriendshipResponse extends GenericResponse<Friendship> {

        /**
         * Default Constructor required by Java.
         * 
         * @param status HTTP Status.
         */
        public FriendshipResponse(final HttpStatus status) {
            super(status);
        }
    };

    /**
     * Helper class for OpenAPI generation.
     * 
     * @author Michael Beutler
     * @version 1.0.0
     * @since 1.0.5
     */
    private final class FriendshipCreatedResponse extends CreatedResponse<Friendship> {
    };

    /**
     * Helper class for OpenAPI generation.
     * 
     * @author Michael Beutler
     * @version 1.0.0
     * @since 1.0.5
     */
    private final class FriendshipUpdatedResponse extends UpdatedResponse<Friendship> {
    };
}