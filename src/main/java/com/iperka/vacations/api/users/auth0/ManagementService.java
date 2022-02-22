package com.iperka.vacations.api.users.auth0;

import java.util.Optional;

import com.auth0.json.mgmt.users.User;
import com.iperka.vacations.api.users.auth0.exceptions.NotConfigured;
import com.iperka.vacations.api.users.exceptions.UserNotFound;

/**
 * The {@link com.iperka.vacations.api.users.auth0.ManagementService}
 * interface defines the basic user operations provided by auth0.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 1.0.0
 */
public interface ManagementService {
    /**
     * Returns user optional with user object provided by Auth0.
     * 
     * Required scopes: users:all:read || users:all:write
     * 
     * @since 1.0.0
     * @param userId Auth0 user id.
     * @return Optional with user object.
     * @throws UserNotFound If user doesn't exists
     */
    public Optional<User> getUserById(final String userId) throws NotConfigured, UserNotFound;

    /**
     * Returns access token for given user object provided by Auth0.
     * 
     * Required scopes: users:all:read || users:all:write
     * 
     * @since 1.0.0
     * @param userId Auth0 user id.
     * @return Access token.
     * @throws UserNotFound If user doesn't exists
     */
    public String getGoogleApiAccessToken(final String userId) throws NotConfigured, UserNotFound;
}
