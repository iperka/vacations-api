package com.iperka.vacations.api.permissions.enums;

/**
 * The {@link PermissionLevel} named enum is required to define privilege level
 * for specific resource. The level names speak for it's self.
 * 
 * @author Michael Beutler
 * @version 1.0.1
 * @since 2021-05-19
 */
public enum PermissionLevel {
    READ("READ"), UPDATE("UPDATE"), DELETE("DELETE");

    String level;

    /**
     * Enum Constructor.
     * 
     * @param level Level String.
     */
    PermissionLevel(String level) {
        this.level = level;
    }
}
