package com.iperka.vacations.api.vacations;

import java.time.Year;
import java.util.Date;
import java.util.List;


import com.iperka.vacations.api.vacations.exceptions.VacationNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The {@link com.iperka.vacations.api.UserService}
 * interface defines the methods that will interact with the
 * {@link com.iperka.vacations.api.UserRepository} interface.
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
     * Returns vacation with given String.
     * Bare in mind that these method should be explicit to administrative
     * roles.
     * 
     * @since 1.0.0
     * @param id String of desired object.
     * @return Vacation object.
     * @throws VacationNotFoundException if vacation could not be found.
     */
    public abstract Vacation findById(String id) throws VacationNotFoundException;

    /**
     * Returns vacation with given String and object must be owned
     * by given user.
     * 
     * @since 1.0.0
     * @param id  String of desired object.
     * @param owner Owner user id provided by Auth0.
     * @return Vacation object.
     * @throws VacationNotFoundException if vacation could not be found.
     */
    public abstract Vacation findByIdAndOwner(String id, String owner) throws VacationNotFoundException;

    /**
     * Returns next vacation for given user relative to given date.
     * 
     * @since 1.0.1
     * @param owner     Owner user id provided by Auth0.
     * @param startDate Start date for relative search.
     * @return Next vacation according to given owner and date.
     * @throws VacationNotFoundException if no vacation could not be found.
     */
    public abstract Vacation findByOwnerAndStartDateGreaterThanEqualOrderByStartDateAsc(String owner, Date startDate)
            throws VacationNotFoundException;

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
     * Deletes vacation with given String.
     * Bare in mind that these method should be explicit to administrative
     * roles.
     * 
     * @since 1.0.0
     * @param id String of desired object.
     * @throws VacationNotFoundException if vacation could not be found.
     */
    public abstract void deleteById(String id) throws VacationNotFoundException;

    /**
     * Deletes vacation with given String and object must be owned
     * by given user.
     * 
     * @since 1.0.0
     * @param id  String of desired object.
     * @param owner Owner user id provided by Auth0.
     * @throws VacationNotFoundException if vacation could not be found.
     */
    public abstract void deleteByIdAndOwner(String id, String owner) throws VacationNotFoundException;

    /**
     * Calculates dates between start and end date and returns a List with the
     * number of days ordered by month.
     * 
     * @since 1.0.0
     * @param vacations List of vacations
     * @param year      Desired year.
     * @return Array of sum's.
     */
    public abstract double[] getDaysCountByMonth(List<Vacation> vacations, Year year);
}
