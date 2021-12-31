package com.iperka.vacations.api.users.auth0.exceptions;

import com.iperka.vacations.api.helpers.APIError;
import com.iperka.vacations.api.helpers.CustomException;

public class NotConfigured extends Exception implements CustomException {
    private static final String MESSAGE = "Auth0 Management API not configured.";
    private static final String CAUSE = "Not able to authenticate with Auth0 Management API.";

    public APIError toApiError() {
        return new APIError("NotConfigured", MESSAGE, CAUSE, null, 404);
    }
}
