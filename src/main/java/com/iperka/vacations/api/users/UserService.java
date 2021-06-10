package com.iperka.vacations.api.users;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * The {@link com.iperka.vacations.api.users.UserService} interface defines the
 * methods that will interact with the
 * {@link com.iperka.vacations.api.users.UserRepository} model.
 * 
 * @author Michael Beutler
 * @version 0.0.1
 * @since 2021-05-07
 */
public interface UserService extends UserDetailsService {

    /**
     * Retrieves all users as {@link org.springframework.data.domain.Page} object.
     * Bare in mind that these method should be explicit to administrative roles.
     */
    public Page<User> findAll(Pageable pageable);

    /**
     * Returns the user object matching the given uuid as
     * {@link java.util.Optional}.
     * 
     * @param uuid Users uuid.
     * @return Optional
     */
    public Optional<User> findByUUID(UUID uuid);

    /**
     * Returns the user object matching the given username as
     * {@link java.util.Optional}.
     * 
     * @param username Users uuid.
     * @return Optional
     */
    public Optional<User> findByUsername(String username);
}
