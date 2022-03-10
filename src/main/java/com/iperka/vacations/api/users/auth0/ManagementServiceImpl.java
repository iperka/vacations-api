package com.iperka.vacations.api.users.auth0;

import java.util.Optional;

import com.auth0.client.auth.AuthAPI;
import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.client.mgmt.filter.UserFilter;
import com.auth0.exception.APIException;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.json.mgmt.users.Identity;
import com.auth0.json.mgmt.users.User;
import com.auth0.net.AuthRequest;
import com.iperka.vacations.api.users.auth0.exceptions.NotConfiguredException;
import com.iperka.vacations.api.users.exceptions.UserNotFoundException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * The
 * {@link com.iperka.vacations.api.users.auth0.ManagementServiceImpl}
 * class implements the
 * {@link com.iperka.vacations.api.users.auth0.ManagementService} interface
 * and is used to manage the users.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 1.0.0
 */
@Service
@Slf4j
public class ManagementServiceImpl implements ManagementService {
    private ManagementAPI managementAPI;
    private TokenHolder holder;

    @Value("${auth0.audience}")
    private String audience;

    @Value("${auth0.domain}")
    private String domain;

    @Value("${auth0.clientId}")
    private String clientId;

    @Value("${auth0.clientSecret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuer;

    private void initialize() throws NotConfiguredException {
        try {
            this.managementAPI = new ManagementAPI(domain, getApiToken());
        } catch (APIException e) {
            throw new NotConfiguredException();
        }

    }

    private String getApiToken() throws APIException {
        AuthAPI authAPI = new AuthAPI(domain, clientId, clientSecret);
        AuthRequest authRequest = authAPI.requestToken(domain + "api/v2/")
                .setScope("read:users").setScope("read:current_user").setScope("read:user_idp_tokens");

        try {
            holder = authRequest.execute();
        } catch (Auth0Exception e) {
            log.error("Exception occur while authentication.", e);
            return "";
        }

        return holder.getAccessToken();
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_users:read', 'SCOPE_users:write', 'SCOPE_users:all:read', 'SCOPE_users:all:write')")
    public Optional<User> getUserById(final String userId) throws NotConfiguredException, UserNotFoundException {
        initialize();
        UserFilter userFilter = new UserFilter();

        try {
            return Optional.of(this.managementAPI.users().get(userId, userFilter).execute());
        } catch (Auth0Exception e) {
            if (e.getMessage().contains("400: Bad HTTP authentication header format")) {
                throw new NotConfiguredException();
            }

            log.error("Exception occur while searching user with userId: <" + userId + ">", e);
            throw new UserNotFoundException();
        }
    }

    @Override
    public String getGoogleApiAccessToken(String userId) throws NotConfiguredException, UserNotFoundException {
        User user = this.getUserById(userId).orElseThrow();
        String accessToken = null;
        for (Identity identity : user.getIdentities()) {
            if (!identity.isSocial()) {
                continue;
            }

            if (identity.getConnection().equalsIgnoreCase("google-oauth2")) {
                accessToken = identity.getAccessToken();
            }
        }

        return accessToken;
    }
}
