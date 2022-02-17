package com.iperka.vacations.api.helpers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class ApiErrorTest {
    @Test
    void shouldReturnCorrectFields() {
        APIError apiError = new APIError();
        assertEquals("unknown", apiError.getType());
        assertEquals("n/a", apiError.getMessage());
        assertEquals(null, apiError.getCause());
        assertEquals(-1, apiError.getCode());
        assertEquals(null, apiError.getField());

        apiError = new APIError("testType", "testMessage", 200);
        assertEquals("testType", apiError.getType());
        assertEquals("testMessage", apiError.getMessage());
        assertEquals(null, apiError.getCause());
        assertEquals(200, apiError.getCode());
        assertEquals(null, apiError.getField());

        apiError = new APIError("testType", "testMessage", "testCause", 200);
        assertEquals("testType", apiError.getType());
        assertEquals("testMessage", apiError.getMessage());
        assertEquals("testCause", apiError.getCause());
        assertEquals(200, apiError.getCode());
        assertEquals(null, apiError.getField());

        apiError = new APIError("testType", "testMessage", "testCause", "testField", 200);
        assertEquals("testType", apiError.getType());
        assertEquals("testMessage", apiError.getMessage());
        assertEquals("testCause", apiError.getCause());
        assertEquals(200, apiError.getCode());
        assertEquals("testField", apiError.getField());
    }

    @Test
    void shouldReturnType() {
        APIError apiError = new APIError();
        assertEquals("unknown", apiError.getType());

        apiError.setType("Test");
        assertEquals("Test", apiError.getType());
    }

    @Test
    void shouldReturnMessage() {
        APIError apiError = new APIError();
        assertEquals("n/a", apiError.getMessage());

        apiError.setMessage("Test");
        assertEquals("Test", apiError.getMessage());
    }

    @Test
    void shouldReturnCause() {
        APIError apiError = new APIError();
        assertEquals(null, apiError.getCause());

        apiError.setCause("Test");
        assertEquals("Test", apiError.getCause());
    }

    @Test
    void shouldReturnCode() {
        APIError apiError = new APIError();
        assertEquals(-1, apiError.getCode());

        apiError.setCode(102);
        assertEquals(102, apiError.getCode());
    }

    @Test
    void shouldReturnField() {
        APIError apiError = new APIError();
        assertEquals(null, apiError.getField());

        apiError.setField("Test");
        assertEquals("Test", apiError.getField());
    }
}