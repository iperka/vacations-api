package com.iperka.vacations.api.helpers;

/**
 * For strict type checking objects can be marked as ownable with this
 * interface.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 2021-12-24
 */
public interface Ownable {
    public String getOwner();
}
