package com.iperka.vacations.api.users.exceptions;

import com.iperka.vacations.api.helpers.APIError;
import com.iperka.vacations.api.helpers.CustomException;

/**
 * Custom exception that will be thrown if user object with given email and or
 * phone already exists.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 1.0.0
 */
public class EmailAndOrPhoneAlreadyConnectedException extends Exception implements CustomException {
    private static final String MESSAGE = "Email and or Phone already connected.";
    private static final String CAUSE = "There is already a connection established with the given email and/or phone.";

    public APIError toApiError() {
        return new APIError("EmailAndOrPhoneAlreadyConnected", MESSAGE, CAUSE, "emailHash,phoneHash", 409);
    }
}