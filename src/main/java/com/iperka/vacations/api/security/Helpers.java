package com.iperka.vacations.api.security;

import com.iperka.vacations.api.helpers.Ownable;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

public class Helpers {
    public static boolean isOwner(final Ownable object, final String userId) {
        return object.getOwner().equals(userId);
    }

    public static boolean isOwner(final Ownable object, final Authentication authentication) {
        final String userId = ((Jwt) authentication.getPrincipal()).getSubject();
        return object.getOwner().equals(userId);
    }

    public static boolean hasScope(final String scope, final Authentication authentication) {
        return (authentication != null
                && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("SCOPE_" + scope)));
    }

    public static String getOwner(final Authentication authentication) {
        return ((Jwt) authentication.getPrincipal()).getSubject();
    }
}
