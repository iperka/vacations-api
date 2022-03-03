package com.iperka.vacations.api.friendships.exceptions;

import com.iperka.vacations.api.helpers.APIError;
import com.iperka.vacations.api.helpers.CustomException;

/**
 * Custom exception that will be thrown if friendship object can not be found.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 1.0.5
 */
public class FriendshipNotFoundException extends Exception implements CustomException {
    private static final String MESSAGE = "Friendship not found.";
    private static final String CAUSE = "There is no friendship matching given uuid.";

    public APIError toApiError() {
        return new APIError("FriendshipNotFound", MESSAGE, CAUSE, null, 404);
    }
}