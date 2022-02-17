package com.iperka.vacations.api.helpers.captcha;

/**
 * Service defining interface for captcha secured resources.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 1.0.0
 */
public interface CaptchaValidatorService {

    /**
     * Validates captcha request.
     * 
     * @param captchaResponse Captcha request.
     * @return true if request is valid.
     */
    public boolean validateCaptcha(String captchaResponse);
}
