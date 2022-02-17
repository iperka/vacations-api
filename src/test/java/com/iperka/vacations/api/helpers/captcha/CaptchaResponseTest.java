package com.iperka.vacations.api.helpers.captcha;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class CaptchaResponseTest {
    @Test
    void shouldReturnCorrectFields() {
        Date date = new Date();
        List<String> errorCodes = new ArrayList<>();
        errorCodes.add("test-error");

        CaptchaResponse captchaResponse = new CaptchaResponse(true, date, "test-host", errorCodes);
        assertTrue(captchaResponse.getSuccess());
        assertEquals(date, captchaResponse.getTimestamp());
        assertEquals("test-host", captchaResponse.getHostname());
        assertEquals(errorCodes, captchaResponse.getErrorCodes());

        captchaResponse = new CaptchaResponse(false, date, "test-host2", errorCodes);
        assertFalse(captchaResponse.getSuccess());
        assertEquals(date, captchaResponse.getTimestamp());
        assertEquals("test-host2", captchaResponse.getHostname());
        assertEquals(errorCodes, captchaResponse.getErrorCodes());
    }
}
