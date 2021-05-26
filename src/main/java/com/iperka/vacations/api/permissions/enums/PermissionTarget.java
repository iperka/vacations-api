package com.iperka.vacations.api.permissions.enums;

import com.iperka.vacations.api.users.User;

/**
 * The {@link PermissionTarget} named enum is required to define privilege
 * target objects for specific resource. Whenever a new model is created a new
 * enum entry has to be created. This approach may not be final.
 * 
 * @author Michael Beutler
 * @version 1.0.1
 * @since 2021-05-19
 */
public enum PermissionTarget {
    USER(User.class);

    Class<?> target;

    /**
     * Enum Constructor.
     * 
     * @param target Target class.
     */
    PermissionTarget(Class<?> target) {
        this.target = target;
    }
}
