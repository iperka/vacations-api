package com.iperka.vacations.api.organizations.exceptions;

import com.iperka.vacations.api.helpers.APIError;
import com.iperka.vacations.api.helpers.CustomException;

public class OrganizationNotFound extends Exception implements CustomException {
    private final static String message = "Organization not found.";
    private final static String cause = "There is now organization matching given query.";

    public APIError toApiError() {
        return new APIError("OrganizationsNotFound", message, cause, null, 404);
    }
}
