package com.iperka.vacations.api.vacations.exceptions;

import com.iperka.vacations.api.helpers.APIError;
import com.iperka.vacations.api.helpers.CustomException;

/**
 * Custom exception that will be thrown if vacation object can not be found.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 1.0.0
 */
public class VacationNotFoundException extends Exception implements CustomException {
    private static final String MESSAGE = "Vacation not found.";
    private static final String CAUSE = "There is no vacation matching given id.";

    public APIError toApiError() {
        return new APIError("VacationNotFound", MESSAGE, CAUSE, null, 404);
    }
}