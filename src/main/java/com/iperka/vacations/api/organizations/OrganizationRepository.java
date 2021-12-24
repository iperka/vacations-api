package com.iperka.vacations.api.organizations;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * The {@link com.iperka.vacations.api.organizations.OrganizationRepository}
 * interface defines the methods that will interact with the
 * {@link com.iperka.vacations.api.organizations.Organization} model.
 * 
 * @author Michael Beutler
 * @version 0.0.4
 * @since 2021-09-29
 */
@Repository
public interface OrganizationRepository extends PagingAndSortingRepository<Organization, UUID> {
    /**
     * Retrieves all organizations as {@link org.springframework.data.domain.Page}
     * object. Bare in mind that these method should be explicit to administrative
     * roles.
     */
    public Page<Organization> findAll(Pageable pageable);

    /**
     * Retrieves all organizations owned by given user as
     * {@link org.springframework.data.domain.Page}
     * object.
     * 
     * @param pageable Pageable object.
     * @param owner    user ID of owner.
     */
    public Page<Organization> findAllByOwner(Pageable pageable, String owner);

    /**
     * Returns the user object matching the given uuid as
     * {@link java.util.Optional}. Bare in mind that these method should be explicit
     * to administrative roles.
     * 
     * @param uuid Organization uuid.
     * @return Optional
     */
    public Optional<Organization> findByUuid(UUID uuid);

    /**
     * Returns the user object matching the given uuid and owner as
     * {@link java.util.Optional}.
     * 
     * @param uuid  Organization uuid.
     * @param owner user ID of owner.
     * @return Optional
     */
    public Optional<Organization> findByUuidAndOwner(UUID uuid, String owner);

    /**
     * Returns the user object matching the given name as
     * {@link java.util.Optional}. Bare in mind that these method should be explicit
     * to administrative roles.
     * 
     * @param name Organization name.
     * @return Optional
     */
    public Optional<Organization> findByNameIgnoreCase(String name);

    /**
     * Returns the user object matching the given name and owner as
     * {@link java.util.Optional}.
     * 
     * @param name  Organization name.
     * @param owner user ID of owner.
     * @return Optional
     */
    public Optional<Organization> findByNameIgnoreCaseAndOwner(String name, String owner);

    /**
     * Returns the organization objects matching the given name as
     * {@link java.util.Page}. Bare in mind that these method should be explicit
     * to administrative roles.
     * 
     * @param pageable Pageable object.
     * @param name     Organization name.
     * @return Page
     */
    public Page<Organization> findByNameContainingIgnoreCase(Pageable pageable, String name);

    /**
     * Returns the organization objects matching the given name and owner as
     * {@link java.util.Page}.
     * 
     * @param pageable Pageable object.
     * @param name     Organization name.
     * @param owner    user ID of owner.
     * @return Page
     */
    public Page<Organization> findByNameContainingIgnoreCaseAndOwner(Pageable pageable, String name, String owner);

    /**
     * Deletes the user object matching the given uuid. Bare in mind that these
     * method should be explicit to administrative roles.
     * 
     * @param uuid  Organization uuid.
     * @param owner user ID of owner.
     */
    public void deleteByUuid(UUID uuid);

    /**
     * Deletes the user object matching the given uuid and owner.
     * 
     * @param uuid  Organization uuid.
     * @param owner user ID of owner.
     */
    public void deleteByUuidAndOwner(UUID uuid, String owner);
}
