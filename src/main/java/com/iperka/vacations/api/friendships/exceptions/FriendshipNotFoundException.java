package com.iperka.vacations.api.friendships.exceptions;

import com.iperka.vacations.api.helpers.APIError;
import com.iperka.vacations.api.helpers.CustomException;

import lombok.extern.slf4j.Slf4j;

/**
 * Custom exception that will be thrown if friendship object can not be found.
 * 
 * @deprecated Concept of friendship is not used anymore.
 * @author Michael Beutler
 * @version 1.0.1
 * @since 1.0.5
 */
@Deprecated
@Slf4j
public class FriendshipNotFoundException extends Exception implements CustomException {
    private static final String MESSAGE = "Friendship not found.";
    private static final String CAUSE = "There is no friendship matching given uuid. (deprecated)";

    public APIError toApiError() {
        log.warn("This exception is deprecated and will be removed in future versions.");
        return new APIError("FriendshipNotFound", MESSAGE, CAUSE, null, 404);
    }
}