package com.iperka.vacations.api.organizations.exceptions;

import com.iperka.vacations.api.helpers.APIError;
import com.iperka.vacations.api.helpers.CustomException;

public class OrganizationNotFound extends Exception implements CustomException {
    private static final String MESSAGE = "Organization not found.";
    private static final String CAUSE = "There is now organization matching given query.";

    public APIError toApiError() {
        return new APIError("OrganizationsNotFound", MESSAGE, CAUSE, null, 404);
    }
}
