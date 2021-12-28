package com.iperka.vacations.api.vacations;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * The {@link com.iperka.vacations.api.vacations.VacationRepository}
 * interface defines the methods that will interact with the
 * {@link com.iperka.vacations.api.vacations.Vacation} model.
 * 
 * @author Michael Beutler
 * @version 0.0.4
 * @since 2021-09-29
 */
@Repository
public interface VacationRepository extends PagingAndSortingRepository<Vacation, UUID> {
    /**
     * Retrieves all vacations as {@link org.springframework.data.domain.Page}
     * object. Bare in mind that these method should be explicit to administrative
     * roles.
     */
    public Page<Vacation> findAll(Pageable pageable);

    /**
     * Retrieves all vacations owned by given user as
     * {@link org.springframework.data.domain.Page}
     * object.
     * 
     * @param pageable Pageable object.
     * @param owner    user ID of owner.
     */
    public Page<Vacation> findAllByOwner(Pageable pageable, String owner);

    /**
     * Returns the user object matching the given uuid as
     * {@link java.util.Optional}. Bare in mind that these method should be explicit
     * to administrative roles.
     * 
     * @param uuid Vacation uuid.
     * @return Optional
     */
    public Optional<Vacation> findByUuid(UUID uuid);

    /**
     * Returns the user object matching the given uuid and owner as
     * {@link java.util.Optional}.
     * 
     * @param uuid  Vacation uuid.
     * @param owner user ID of owner.
     * @return Optional
     */
    public Optional<Vacation> findByUuidAndOwner(UUID uuid, String owner);

    /**
     * Returns the user object matching the given name as
     * {@link java.util.Optional}. Bare in mind that these method should be explicit
     * to administrative roles.
     * 
     * @param name Vacation name.
     * @return Optional
     */
    public Optional<Vacation> findByNameIgnoreCase(String name);

    /**
     * Returns the user object matching the given name and owner as
     * {@link java.util.Optional}.
     * 
     * @param name  Vacation name.
     * @param owner user ID of owner.
     * @return Optional
     */
    public Optional<Vacation> findByNameIgnoreCaseAndOwner(String name, String owner);

    /**
     * Returns the vacation objects matching the given name as
     * {@link java.util.Page}. Bare in mind that these method should be explicit
     * to administrative roles.
     * 
     * @param pageable Pageable object.
     * @param name     Vacation name.
     * @return Page
     */
    public Page<Vacation> findByNameContainingIgnoreCase(Pageable pageable, String name);

    /**
     * Returns the vacation objects matching the given name and owner as
     * {@link java.util.Page}.
     * 
     * @param pageable Pageable object.
     * @param name     Vacation name.
     * @param owner    user ID of owner.
     * @return Page
     */
    public Page<Vacation> findByNameContainingIgnoreCaseAndOwner(Pageable pageable, String name, String owner);

    /**
     * Deletes the user object matching the given uuid. Bare in mind that these
     * method should be explicit to administrative roles.
     * 
     * @param uuid  Vacation uuid.
     * @param owner user ID of owner.
     */
    public void deleteByUuid(UUID uuid);

    /**
     * Deletes the user object matching the given uuid and owner.
     * 
     * @param uuid  Vacation uuid.
     * @param owner user ID of owner.
     */
    public void deleteByUuidAndOwner(UUID uuid, String owner);
}
