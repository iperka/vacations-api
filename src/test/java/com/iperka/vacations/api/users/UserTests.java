package com.iperka.vacations.api.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserTests {

    @Test
    void shouldSetFields() {
        User user = new User("Test", "TestPassword");
        assertEquals("Test", user.getUsername());
        assertEquals("TestPassword", user.getPassword());
    }

    @Test
    void shouldReturnUUID() {
        User user = new User();
        UUID uuid = UUID.randomUUID();

        user.setUuid(uuid);
        assertEquals(uuid, user.getUuid());
    }

    @Test
    void shouldReturnUsername() {
        User user = new User();
        String username = "Test";

        user.setUsername(username);
        assertEquals(username, user.getUsername());
    }

    @Test
    void shouldReturnPassword() {
        User user = new User();
        String password = "Test";

        user.setPassword(password);
        assertEquals(password, user.getPassword());
    }

    @Test
    void shouldReturnEnabled() {
        User user = new User();
        assertFalse(user.isEnabled());

        user.setEnabled(true);
        assertTrue(user.isEnabled());
    }

    @Test
    void shouldReturnLocked() {
        User user = new User();
        assertFalse(user.isLocked());

        user.setLocked(true);
        assertTrue(user.isLocked());
    }

    @Test
    void shouldReturnExpired() {
        User user = new User();
        assertFalse(user.isExpired());

        user.setExpired(true);
        assertTrue(user.isExpired());
    }

    @Test
    void shouldReturnCredentialsExpired() {
        User user = new User();
        assertFalse(user.isCredentialsExpired());

        user.setCredentialsExpired(true);
        assertTrue(user.isCredentialsExpired());
    }

    @Test
    void shouldReturnCreated() {
        User user = new User();
        Date date = new Date();

        user.setCreated(date);
        assertEquals(date, user.getCreated());
    }

    @Test
    void shouldReturnUpdated() {
        User user = new User();
        Date date = new Date();

        user.setUpdated(date);
        assertEquals(date, user.getUpdated());
    }
}
