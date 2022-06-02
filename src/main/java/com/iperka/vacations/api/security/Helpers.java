package com.iperka.vacations.api.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

// import com.iperka.vacations.api.friendships.Friendship;
// import com.iperka.vacations.api.friendships.FriendshipStatus;
import com.iperka.vacations.api.helpers.Ownable;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Helpers {
    public static boolean isOwner(final Ownable object, final String userId) {
        return object.getOwner().equals(userId);
    }

    public static boolean isOwner(final Ownable object, final Authentication authentication) {
        final String userId = getUserId(authentication);
        return object.getOwner().equals(userId);
    }

    public static boolean hasScope(final String scope, final Authentication authentication) {
        return (authentication != null
                && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("SCOPE_" + scope)));
    }

    public static String getUserId(final Authentication authentication) {
        try {
            return ((Jwt) authentication.getPrincipal()).getSubject();
        } catch (ClassCastException e) {
            log.error("Exception while reading userId:", e);
            return authentication.getName();
        }
    }

    private Helpers() {
    }
}
