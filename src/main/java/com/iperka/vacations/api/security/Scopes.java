package com.iperka.vacations.api.security;

/**
 * The {@link com.iperka.vacations.api.security.Scopes} class defines
 * the security scope constants for better constancy.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 2021-12-27
 */
public class Scopes {
    private static final String SCOPE_PREFIX = "SCOPE_";

    public static final String ORGANIZATIONS_READ = "organizations:read";
    public static final String SCOPE_ORGANIZATIONS_READ = SCOPE_PREFIX + ORGANIZATIONS_READ;

    public static final String ORGANIZATIONS_WRITE = "organizations:write";
    public static final String SCOPE_ORGANIZATIONS_WRITE = SCOPE_PREFIX + ORGANIZATIONS_WRITE;

    public static final String ORGANIZATIONS_ALL_READ = "organizations:all:read";
    public static final String SCOPE_ORGANIZATIONS_ALL_READ = SCOPE_PREFIX + ORGANIZATIONS_ALL_READ;

    public static final String ORGANIZATIONS_ALL_WRITE = "organizations:all:write";
    public static final String SCOPE_ORGANIZATIONS_ALL_WRITE = SCOPE_PREFIX + ORGANIZATIONS_ALL_WRITE;

    public static final String VACATIONS_READ = "vacations:read";
    public static final String SCOPE_VACATIONS_READ = SCOPE_PREFIX + VACATIONS_READ;

    public static final String VACATIONS_WRITE = "vacations:write";
    public static final String SCOPE_VACATIONS_WRITE = SCOPE_PREFIX + VACATIONS_WRITE;

    public static final String VACATIONS_ALL_READ = "vacations:all:read";
    public static final String SCOPE_VACATIONS_ALL_READ = SCOPE_PREFIX + VACATIONS_ALL_READ;

    public static final String VACATIONS_ALL_WRITE = "vacations:all:write";
    public static final String SCOPE_VACATIONS_ALL_WRITE = SCOPE_PREFIX + VACATIONS_ALL_WRITE;

    public static final String USERS_ALL_READ = "users:all:read";
    public static final String SCOPE_USERS_ALL_READ = SCOPE_PREFIX + USERS_ALL_READ;

    public static final String USERS_ALL_WRITE = "users:all:write";
    public static final String SCOPE_USERS_ALL_WRITE = SCOPE_PREFIX + USERS_ALL_WRITE;

    private Scopes() {
    }
}
