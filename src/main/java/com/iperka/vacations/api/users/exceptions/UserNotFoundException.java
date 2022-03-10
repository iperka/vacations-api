package com.iperka.vacations.api.users.exceptions;

import com.iperka.vacations.api.helpers.APIError;
import com.iperka.vacations.api.helpers.CustomException;

public class UserNotFoundException extends Exception implements CustomException {
    private static final String MESSAGE = "User not found.";
    private static final String CAUSE = "There is no user matching given query.";

    public APIError toApiError() {
        return new APIError("UserNotFound", MESSAGE, CAUSE, null, 404);
    }
}
