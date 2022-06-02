package com.iperka.vacations.api.friendships.exceptions;

import com.iperka.vacations.api.helpers.APIError;
import com.iperka.vacations.api.helpers.CustomException;

import lombok.extern.slf4j.Slf4j;

/**
 * Custom exception that will be thrown if friendship relation object already
 * exists.
 * 
 * @deprecated Concept of friendship is not used anymore.
 * @author Michael Beutler
 * @version 1.0.1
 * @since 1.0.6
 */
@Deprecated
@Slf4j
public class FriendshipRelationAlreadyExistsException extends Exception implements CustomException {
    private static final String MESSAGE = "Friendship relation already exists.";
    private static final String CAUSE = "There is already a friendship relation object matching owner and user. (deprecated)";

    public APIError toApiError() {
        log.warn("This exception is deprecated and will be removed in future versions.");
        return new APIError("FriendshipRelationAlreadyExists", MESSAGE, CAUSE, "owner, user", 409);
    }
}