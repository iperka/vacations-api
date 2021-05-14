package com.iperka.vacations.api.users.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.iperka.vacations.api.users.User;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserDTOTests {
    @Test
    void shouldSetFields() {
        UserDTO dto = new UserDTO("Test", "TestPassword");

        assertEquals("Test", dto.getUsername());
        assertEquals("TestPassword", dto.getPassword());
    }

    @Test
    void shouldReturnCorrectEntityObject() {
        UserDTO dto = new UserDTO("Test", "TestPassword");
        User user = dto.toObject();

        assertNotNull(user.getUuid());
        assertEquals("Test", user.getUsername());
        assertNull(user.getPassword());
    }
}
