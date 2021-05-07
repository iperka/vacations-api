package com.iperka.vacations.api.users;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void shouldThrowsUsernameNotFoundException() {
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("invalid");
        });
    }

}
