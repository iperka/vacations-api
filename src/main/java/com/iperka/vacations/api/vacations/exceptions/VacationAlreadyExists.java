package com.iperka.vacations.api.vacations.exceptions;

import com.iperka.vacations.api.helpers.APIError;
import com.iperka.vacations.api.helpers.CustomException;

public class VacationAlreadyExists extends Exception implements CustomException {
    private static final String MESSAGE = "Vacation already exists.";
    private static final String CAUSE = "There is already an vacation with a similar name.";

    public APIError toApiError() {
        return new APIError("VacationAlreadyExists", MESSAGE, CAUSE, "name", 400);
    }
}
