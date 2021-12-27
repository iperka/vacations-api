package com.iperka.vacations.api.security;

/**
 * The {@link com.iperka.vacations.api.security.Scopes} class defines
 * the security scope constants for better constancy.
 * 
 * @author Michael Beutler
 * @version 0.0.1
 * @since 2021-12-27
 */
public class Scopes {
    private static final String SCOPE_PREFIX = "SCOPE_";

    public static final String ORGANIZATIONS_READ = "organizations:read";
    public static final String SCOPE_ORGANIZATIONS_READ = SCOPE_PREFIX + ORGANIZATIONS_READ;

    public static final String ORGANIZATION_WRITE = "organizations:write";
    public static final String SCOPE_ORGANIZATION_WRITE = SCOPE_PREFIX + ORGANIZATION_WRITE;

    public static final String ORGANIZATIONS_ALL_READ = "organizations:all:read";
    public static final String SCOPE_ORGANIZATIONS_ALL_READ = SCOPE_PREFIX + ORGANIZATIONS_ALL_READ;

    public static final String ORGANIZATION_ALL_WRITE = "organizations:all:write";
    public static final String SCOPE_ORGANIZATION_ALL_WRITE = SCOPE_PREFIX + ORGANIZATION_ALL_WRITE;
}
