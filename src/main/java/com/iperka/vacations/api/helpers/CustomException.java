package com.iperka.vacations.api.helpers;

/**
 * Helper interface for stricter custom exceptions.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 1.0.0
 */
public interface CustomException {
    public APIError toApiError();
}
