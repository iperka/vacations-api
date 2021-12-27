package com.iperka.vacations.api.organizations.exceptions;

import com.iperka.vacations.api.helpers.APIError;
import com.iperka.vacations.api.helpers.CustomException;

public class OrganizationAlreadyExists extends Exception implements CustomException {
    private final static String message = "Organization already exists.";
    private final static String cause = "There is already an organization with a similar name.";

    public APIError toApiError() {
        return new APIError("OrganizationsAlreadyExists", message, cause, "name", 400);
    }
}
