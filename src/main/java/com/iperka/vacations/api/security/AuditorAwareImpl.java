package com.iperka.vacations.api.security;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * AuditorAware implementation for extracting user id of
 * {@link SecurityContextHolder}.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 1.0.0
 */
public class AuditorAwareImpl implements AuditorAware<String> {

    /**
     * Returns current auditor provided by {@link SecurityContextHolder}.
     * 
     * @since 1.0.0
     * @return Optional of user id as string.
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getName);
    }
}
