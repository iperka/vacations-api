package com.iperka.vacations.api.audit.exceptions;

import com.iperka.vacations.api.helpers.APIError;
import com.iperka.vacations.api.helpers.CustomException;

/**
 * Custom exception that will be thrown if organization object can not be found.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 1.0.0
 */
public class AuditNotFoundException extends Exception implements CustomException {
    private static final String MESSAGE = "Audit log not found.";
    private static final String CAUSE = "There is no audit log matching given id.";

    public APIError toApiError() {
        return new APIError("AuditNotFoundException", MESSAGE, CAUSE, null, 404);
    }
}
