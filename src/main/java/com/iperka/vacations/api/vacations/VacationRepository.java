package com.iperka.vacations.api.vacations;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * The {@link com.iperka.vacations.api.VacationRepository}
 * interface defines the methods that will interact with the
 * {@link com.iperka.vacations.api.Vacation} model.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 1.0.0
 */
@Repository
public interface VacationRepository extends PagingAndSortingRepository<Vacation, UUID> {
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
     * Returns vacation with given UUID.
     * Bare in mind that these method should be explicit to administrative
     * roles.
     * 
     * @since 1.0.0
     * @param uuid UUID of desired object.
     * @return Optional with Vacation object.
     */
    public Optional<Vacation> findByUuid(UUID uuid);

    /**
     * Returns vacation with given UUID and object must be owned
     * by given user.
     * 
     * @since 1.0.0
     * @param uuid  UUID of desired object.
     * @param owner Owner user id provided by Auth0.
     * @return Optional with Vacation object.
     */
    public Optional<Vacation> findByUuidAndOwner(UUID uuid, String owner);

    /**
     * Deletes vacation with given UUID.
     * Bare in mind that these method should be explicit to administrative
     * roles.
     * 
     * @since 1.0.0
     * @param uuid UUID of desired object.
     */
    public void deleteByUuid(UUID uuid);

    /**
     * Deletes vacation with given UUID and object must be owned
     * by given user.
     * 
     * @since 1.0.0
     * @param uuid  UUID of desired object.
     * @param owner Owner user id provided by Auth0.
     */
    public void deleteByUuidAndOwner(UUID uuid, String owner);
}
