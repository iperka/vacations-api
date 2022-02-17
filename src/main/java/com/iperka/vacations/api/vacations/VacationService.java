package com.iperka.vacations.api.vacations;

import java.time.Year;
import java.util.List;
import java.util.UUID;

import com.iperka.vacations.api.vacations.exceptions.VacationNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The {@link com.iperka.vacations.api.VacationService}
 * interface defines the methods that will interact with the
 * {@link com.iperka.vacations.api.VacationRepository} interface.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 1.0.0
 */
public interface VacationService {
    /**
     * Retrieves all vacations as {@link org.springframework.data.domain.Page}
     * object. Bare in mind that these method should be explicit to administrative
     * roles.
     * 
     * @since 1.0.0
     * @param pageable Pageable object.
     * @return Optional Page with Vacation objects.
     */
    public abstract Page<Vacation> findAll(Pageable pageable);

    /**
     * Retrieves all vacations owned by given user as
     * {@link org.springframework.data.domain.Page} object.
     * 
     * @since 1.0.0
     * @param pageable Pageable object.
     * @param owner    Owner user id provided by Auth0.
     * @return Optional Page with Vacation objects.
     */
    public abstract Page<Vacation> findAllByOwner(Pageable pageable, String owner);

    /**
     * Returns vacation with given UUID.
     * Bare in mind that these method should be explicit to administrative
     * roles.
     * 
     * @since 1.0.0
     * @param uuid UUID of desired object.
     * @return Vacation object.
     * @throws VacationNotFoundException if vacation could not be found.
     */
    public abstract Vacation findByUuid(UUID uuid) throws VacationNotFoundException;

    /**
     * Returns vacation with given UUID and object must be owned
     * by given user.
     * 
     * @since 1.0.0
     * @param uuid  UUID of desired object.
     * @param owner Owner user id provided by Auth0.
     * @return Vacation object.
     * @throws VacationNotFoundException if vacation could not be found.
     */
    public abstract Vacation findByUuidAndOwner(UUID uuid, String owner) throws VacationNotFoundException;

    /**
     * Creates and returns vacation.
     * 
     * @since 1.0.0
     * @param vacation new object.
     * @return Vacation created object.
     * @throws VacationNotFoundException if vacation could not be found.
     */
    public abstract Vacation create(Vacation vacation);

    /**
     * Updates and returns vacation with given object.
     * 
     * @since 1.0.0
     * @param vacation new object.
     * @return Vacation updated object.
     * @throws VacationNotFoundException if vacation could not be found.
     */
    public abstract Vacation update(Vacation vacation) throws VacationNotFoundException;

    /**
     * Updates and returns vacation with given object if owner equals given owner.
     * 
     * @since 1.0.0
     * @param vacation new object.
     * @param owner    Owner user id provided by Auth0.
     * @return Vacation updated object.
     * @throws VacationNotFoundException if vacation could not be found.
     */
    public abstract Vacation updateByOwner(Vacation vacation, String owner) throws VacationNotFoundException;

    /**
     * Deletes vacation with given UUID.
     * Bare in mind that these method should be explicit to administrative
     * roles.
     * 
     * @since 1.0.0
     * @param uuid UUID of desired object.
     * @throws VacationNotFoundException if vacation could not be found.
     */
    public abstract void deleteByUuid(UUID uuid) throws VacationNotFoundException;

    /**
     * Deletes vacation with given UUID and object must be owned
     * by given user.
     * 
     * @since 1.0.0
     * @param uuid  UUID of desired object.
     * @param owner Owner user id provided by Auth0.
     * @throws VacationNotFoundException if vacation could not be found.
     */
    public abstract void deleteByUuidAndOwner(UUID uuid, String owner) throws VacationNotFoundException;

    /**
     * Calculates dates between start and end date and returns a List with the
     * number of days ordered by month.
     * 
     * @since 1.0.0
     * @param vacations List of vacations
     * @param year Desired year.
     * @return Array of sum's.
     */
    public double[] getDaysCountByMonth(List<Vacation> vacations, Year year);
}
