package com.iperka.vacations.api.friendships.exceptions;

import com.iperka.vacations.api.helpers.APIError;
import com.iperka.vacations.api.helpers.CustomException;

/**
 * Custom exception that will be thrown if friendship relation object already
 * exists.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 1.0.6
 */
public class FriendshipRelationAlreadyExistsException extends Exception implements CustomException {
    private static final String MESSAGE = "Friendship relation already exists.";
    private static final String CAUSE = "There is already a friendship relation object matching owner and user.";

    public APIError toApiError() {
        return new APIError("FriendshipRelationAlreadyExists", MESSAGE, CAUSE, "owner, user", 409);
    }
}