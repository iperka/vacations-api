package com.iperka.vacations.api.security;

import java.util.List;

// import com.iperka.vacations.api.friendships.Friendship;
// import com.iperka.vacations.api.friendships.FriendshipStatus;
import com.iperka.vacations.api.helpers.Ownable;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

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

    @Deprecated
    public static boolean isFriend(final Authentication authentication, final String user,
            List<?> friendship) {
        // if (friendship.size() != 2) {
        // return false;
        // }
        // for (Friendship f : friendship) {
        // if (!f.getStatus().equals(FriendshipStatus.ACCEPTED)) {
        // return false;
        // }
        // if (!f.getUser().equals(Helpers.getUserId(authentication))
        // && !f.getOwner().equals(Helpers.getUserId(authentication))) {
        // return false;
        // }
        // }
        return true;
    }

    private Helpers() {
    }
}
