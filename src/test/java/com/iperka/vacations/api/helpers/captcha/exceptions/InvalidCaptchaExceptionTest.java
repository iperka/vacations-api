package com.iperka.vacations.api.helpers.captcha.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.iperka.vacations.api.helpers.APIError;
import com.iperka.vacations.api.helpers.CustomException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class InvalidCaptchaExceptionTest {
    @Test
    void shouldReturnApiError() {
        CustomException exception = new InvalidCaptchaException();
        APIError apiError = exception.toApiError();
        assertEquals("InvalidCaptcha", apiError.getType());
        assertEquals("Captcha invalid.", apiError.getMessage());
        assertEquals("Given captcha is considered invalid.", apiError.getCause());
        assertEquals("headers.captcha-response", apiError.getField());
        assertEquals(400, apiError.getCode());
    }
}
