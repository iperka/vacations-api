package com.iperka.vacations.api.helpers.captcha;

import javax.servlet.http.HttpServletRequest;

import com.iperka.vacations.api.helpers.CustomException;
import com.iperka.vacations.api.helpers.GenericResponse;
import com.iperka.vacations.api.helpers.captcha.exceptions.InvalidCaptchaException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Google Captcha aspect allowing to validate requests with captcha
 * requirements. Checks the header 'captcha-response'.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 1.0.0
 */
@Aspect
@Component
public class CaptchaAspect {

    @Autowired
    private CaptchaValidatorService captchaValidatorService;

    private static final String CAPTCHA_HEADER_NAME = "captcha-response";

    /**
     * Wrap's around {@link RequiresCaptcha} annotation. Returns a error response if
     * request is invalid.
     * 
     * @param joinPoint {@link ProceedingJoinPoint} provided by Spring.
     * @return Error response or will proceed as normal.
     * @throws Throwable Can throw exception.
     */
    @Around("@annotation(RequiresCaptcha)")
    public Object validateCaptcha(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        String captchaResponse = request.getHeader(CAPTCHA_HEADER_NAME);
        boolean isValidCaptcha = captchaValidatorService.validateCaptcha(captchaResponse);
        
        if (!isValidCaptcha) {
            CustomException ex = new InvalidCaptchaException();
            final GenericResponse<Object> response = new GenericResponse<>(HttpStatus.BAD_REQUEST);
            response.addError(ex.toApiError());

            return response.build();
        }
        return joinPoint.proceed();
    }

}
