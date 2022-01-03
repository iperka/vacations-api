package com.iperka.vacations.api.users.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.iperka.vacations.api.helpers.APIError;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserNotFoundTest {
    @Test
    void shouldReturnCorrectFields() {
        UserNotFound userNotFound = new UserNotFound();

        APIError apiError = new APIError("UserNotFound", "User not found.", "There is no user matching given query.",
                null, 404);

        assertEquals(apiError.getMessage(), userNotFound.toApiError().getMessage());
        assertEquals(apiError.getCause(), userNotFound.toApiError().getCause());
        assertEquals(apiError.getType(), userNotFound.toApiError().getType());
        assertEquals(apiError.getCode(), userNotFound.toApiError().getCode());
    }
}
