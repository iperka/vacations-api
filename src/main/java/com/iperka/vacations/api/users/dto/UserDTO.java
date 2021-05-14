package com.iperka.vacations.api.users.dto;

import java.util.UUID;

import com.iperka.vacations.api.helpers.DTO;
import com.iperka.vacations.api.users.User;

/**
 * The {@link com.iperka.vacations.api.users.dto.UserDTO} class defines the
 * structure of a basic user DTO. This includes numerous attributes used for
 * authentication, authorization and specification of user details and states.
 * 
 * Use this DTO to create new users. Keep in mind that the password field
 * requires some extra action. It has to be hashed by your predefined password
 * encoder. This should be done on the service layer.
 * 
 * @author Michael Beutler
 * @version 0.0.3
 * @since 2021-05-12
 */
public class UserDTO implements DTO<User> {
    private final String username;
    private final String password;

    /**
     * Create user DTO with username and password. All the other fields will be set
     * to default.
     * 
     * @param username Username for new user.
     * @param password Password for new user.
     */
    public UserDTO(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Transforms DTO to User model class. In this case one field has to be set
     * manually. Set the password by yourself using the preferred password encoder.
     * 
     * Warning: This method doesn't check if the user already exists!
     * 
     * @return User entity object.
     */
    @Override
    public User toObject() {
        final User user = new User();

        // Set user fields
        user.setUuid(UUID.randomUUID());
        user.setUsername(username);
        user.setEnabled(false);
        user.setLocked(false);
        user.setExpired(false);
        user.setCredentialsExpired(false);

        return user;
    }

    /**
     * Returns the plain password for user DTO.
     * 
     * @return Plain password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the username for user DTO.
     * 
     * @return Username.
     */
    public String getUsername() {
        return username;
    }

}
