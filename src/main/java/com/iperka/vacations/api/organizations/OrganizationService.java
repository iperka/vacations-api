package com.iperka.vacations.api.organizations;

import java.util.Optional;
import java.util.UUID;

import com.iperka.vacations.api.organizations.exceptions.OrganizationAlreadyExists;
import com.iperka.vacations.api.organizations.exceptions.OrganizationNotFound;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The {@link com.iperka.vacations.api.organizations.OrganizationService}
 * interface defines the methods that will interact with the
 * {@link com.iperka.vacations.api.organisations.OrganisationRepository} model.
 * 
 * @author Michael Beutler
 * @version 0.0.7
 * @since 2021-09-29
 */
public interface OrganizationService {
    /**
     * Retrieves all organizations as {@link org.springframework.data.domain.Page}
     * object. Bare in mind that these method should be explicit to administrative
     * roles.
     * 
     * Required scopes: organizations:all:read || organizations:all:write
     * 
     * @return Page object.
     */
    public Page<Organization> findAll(Pageable pageable);

    /**
     * Retrieves all organizations as {@link org.springframework.data.domain.Page}
     * object.
     * 
     * Required scopes: organizations:read || organizations:write
     * 
     * @return Page object.
     */
    public Page<Organization> findAllByOwner(Pageable pageable, String owner);

    /**
     * Retrieves all organizations matching the given name as
     * {@link org.springframework.data.domain.Page} object. Bare in mind that these
     * method should be explicit to administrative roles.
     * 
     * Required scopes: organizations:all:read || organizations:all:write
     * 
     * @return Page object.
     */
    public Page<Organization> findByNameContainingIgnoreCase(Pageable pageable, String name);

    /**
     * Retrieves all organizations matching the given name as
     * {@link org.springframework.data.domain.Page} object.
     * 
     * Required scopes: organizations:read || organizations:write
     * 
     * @return Page object.
     */
    public Page<Organization> findByNameContainingIgnoreCaseAndOwner(Pageable pageable, String name, String owner);

    /**
     * Returns the organization object matching the given uuid as
     * {@link java.util.Optional}. Bare in mind that these
     * method should be explicit to administrative roles.
     * 
     * Required scopes: organizations:all:read || organizations:all:write
     * 
     * @param uuid Objects uuid.
     * @return Optional
     */
    public Organization findByUuid(UUID uuid) throws OrganizationNotFound;

    /**
     * Returns the organization object matching the given uuid as
     * {@link java.util.Optional}.
     * 
     * Required scopes: organizations:read || organizations:write
     * 
     * @param uuid Objects uuid.
     * @return Optional
     */
    public Organization findByUuidAndOwner(UUID uuid, String owner) throws OrganizationNotFound;

    /**
     * Returns the organization object matching the given name as
     * {@link java.util.Optional}. Bare in mind that these
     * method should be explicit to administrative roles.
     * 
     * Required scopes: organizations:all:read || organizations:all:write
     * 
     * @param name Objects name.
     * @return Optional
     */
    public Optional<Organization> findByNameIgnoreCase(String name);

    /**
     * Returns the organization object matching the given name as
     * {@link java.util.Optional}.
     * Required scopes: organizations:read || organizations:write
     * 
     * @param name Objects name.
     * @return Optional
     */
    public Optional<Organization> findByNameIgnoreCaseAndOwner(String name, String owner);

    /**
     * Saves a given object to database. This will create a new one if it doesn't
     * exists.
     * 
     * Required scopes: organizations:write
     * 
     * @param organization Organization object.
     * @return created organization
     */
    public Organization create(Organization organization) throws OrganizationAlreadyExists;

    /**
     * Deletes the organization object matching the given uuid. Bare in mind that
     * these method should be explicit to administrative roles.
     * 
     * Required scopes: organizations:all:write
     * 
     * @param uuid Objects uuid.
     */
    public void deleteByUuid(UUID uuid) throws OrganizationNotFound;

    /**
     * Deletes the organization object matching the given uuid.
     * 
     * Required scopes: organizations:write
     * 
     * @param uuid Objects uuid.
     */
    public void deleteByUuidAndOwner(UUID uuid, String owner) throws OrganizationNotFound;
}
