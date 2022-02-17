package com.iperka.vacations.api.vacations.exceptions;

import com.iperka.vacations.api.helpers.APIError;
import com.iperka.vacations.api.helpers.CustomException;

public class VacationInvalidDateRangeException extends Exception implements CustomException {
    private static final String MESSAGE = "Invalid date range provided.";
    private static final String CAUSE = "Start date is larger then end date.";

    public APIError toApiError() {
        return new APIError("VacationInvalidDateRange", MESSAGE, CAUSE, null, 404);
    }
}
