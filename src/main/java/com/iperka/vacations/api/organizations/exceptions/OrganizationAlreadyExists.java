package com.iperka.vacations.api.organizations.exceptions;

import com.iperka.vacations.api.helpers.APIError;
import com.iperka.vacations.api.helpers.CustomException;

public class OrganizationAlreadyExists extends Exception implements CustomException {
    private static final String MESSAGE = "Organization already exists.";
    private static final String CAUSE = "There is already an organization with a similar name.";

    public APIError toApiError() {
        return new APIError("OrganizationsAlreadyExists", MESSAGE, CAUSE, "name", 400);
    }
}
