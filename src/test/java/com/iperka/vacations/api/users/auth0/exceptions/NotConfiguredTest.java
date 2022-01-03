package com.iperka.vacations.api.users.auth0.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.iperka.vacations.api.helpers.APIError;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NotConfiguredTest {
    @Test
    void shouldReturnCorrectFields() {
        NotConfigured notConfigured = new NotConfigured();

        APIError apiError = new APIError("NotConfigured", "Auth0 Management API not configured.",
                "Not able to authenticate with Auth0 Management API.", null, 404);

        assertEquals(apiError.getMessage(), notConfigured.toApiError().getMessage());
        assertEquals(apiError.getCause(), notConfigured.toApiError().getCause());
        assertEquals(apiError.getType(), notConfigured.toApiError().getType());
        assertEquals(apiError.getCode(), notConfigured.toApiError().getCode());
    }
}
