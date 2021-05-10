package com.iperka.vacations.api.users;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldThrowUsernameNotFoundException() {
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("invalid");
        });

        assertTrue(exception.getMessage().contains("invalid"));
    }

    @Test
    void shouldReturnOptional() {
        Optional<User> uOptional = userService.findByUUID(UUID.randomUUID());
        assertFalse(uOptional.isPresent());
    }

}
