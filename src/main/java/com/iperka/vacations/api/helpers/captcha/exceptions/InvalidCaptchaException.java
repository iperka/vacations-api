package com.iperka.vacations.api.helpers.captcha.exceptions;


import com.iperka.vacations.api.helpers.APIError;
import com.iperka.vacations.api.helpers.CustomException;

/**
 * Custom exception that will be thrown if captcha response is invalid.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 1.0.0
 */
public class InvalidCaptchaException extends Exception implements CustomException {
    private static final String MESSAGE = "Captcha invalid.";
    private static final String CAUSE = "Given captcha is considered invalid.";

    public APIError toApiError() {
        return new APIError("InvalidCaptcha", MESSAGE, CAUSE, "headers.captcha-response", 400);
    }
}

