package com.iperka.vacations.api.vacations;

import java.util.Date;
import java.util.Optional;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * The {@link com.iperka.vacations.api.UserRepository}
 * interface defines the methods that will interact with the
 * {@link com.iperka.vacations.api.Vacation} model.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 1.0.0
 */
@Repository
public interface VacationRepository extends PagingAndSortingRepository<Vacation, String> {
    /**
     * Retrieves all vacations as {@link org.springframework.data.domain.Page}
     * object. Bare in mind that these method should be explicit to administrative
     * roles.
     * 
     * @since 1.0.0
     * @param pageable Pageable object.
     * @return Optional Page with Vacation objects.
     */
    public Page<Vacation> findAll(Pageable pageable);

    /**
     * Retrieves all vacations owned by given user as
     * {@link org.springframework.data.domain.Page} object.
     * 
     * @since 1.0.0
     * @param pageable Pageable object.
     * @param owner    Owner user id provided by Auth0.
     * @return Optional Page with Vacation objects.
     */
    public Page<Vacation> findAllByOwner(Pageable pageable, String owner);

    /**
     * Returns vacation with given String.
     * Bare in mind that these method should be explicit to administrative
     * roles.
     * 
     * @since 1.0.0
     * @param id String of desired object.
     * @return Optional with Vacation object.
     */
    public Optional<Vacation> findById(String id);

    /**
     * Returns vacation with given String and object must be owned
     * by given user.
     * 
     * @since 1.0.0
     * @param id  String of desired object.
     * @param owner Owner user id provided by Auth0.
     * @return Optional with Vacation object.
     */
    public Optional<Vacation> findByIdAndOwner(String id, String owner);

    /**
     * Returns next vacation for given user relative to given date.
     * 
     * @since 1.0.1
     * @param owner     Owner user id provided by Auth0.
     * @param startDate Start date for relative search.
     * @return Next vacation according to given owner and date.
     */
    public Optional<Vacation> findByOwnerAndStartDateGreaterThanEqualOrderByStartDateAsc(String owner, Date startDate);

    /**
     * Deletes vacation with given String.
     * Bare in mind that these method should be explicit to administrative
     * roles.
     * 
     * @since 1.0.0
     * @param id String of desired object.
     */
    public void deleteById(String id);

    /**
     * Deletes vacation with given String and object must be owned
     * by given user.
     * 
     * @since 1.0.0
     * @param id  String of desired object.
     * @param owner Owner user id provided by Auth0.
     */
    public void deleteByIdAndOwner(String id, String owner);
}
