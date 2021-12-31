package com.iperka.vacations.api.vacations.exceptions;

import com.iperka.vacations.api.helpers.APIError;
import com.iperka.vacations.api.helpers.CustomException;

public class VacationNotFound extends Exception implements CustomException {
    private static final String MESSAGE = "Vacation not found.";
    private static final String CAUSE = "There is no vacation matching given query.";

    public APIError toApiError() {
        return new APIError("VacationNotFound", MESSAGE, CAUSE, null, 404);
    }
}
