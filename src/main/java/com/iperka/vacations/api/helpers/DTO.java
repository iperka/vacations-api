package com.iperka.vacations.api.helpers;

/**
 * Very basic DTO interface. Implement this interface with each DTO class.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 2021-05-14
 */
public interface DTO<T> {

    /**
     * Get entity object from DTO.
     * 
     * @return Entity.
     */
    public T toObject();
}
