package com.iperka.vacations.api.organizations;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The {@link com.iperka.vacations.api.organizations.OrganizationService}
 * interface defines the methods that will interact with the
 * {@link com.iperka.vacations.api.organisations.OrganisationRepository} model.
 * 
 * @author Michael Beutler
 * @version 0.0.4
 * @since 2021-09-29
 */
public interface OrganizationService {
    /**
     * Retrieves all organizations as {@link org.springframework.data.domain.Page}
     * object. Bare in mind that these method should be explicit to administrative
     * roles.
     */
    public Page<Organization> findAll(Pageable pageable);

    /**
     * Returns the user object matching the given uuid as
     * {@link java.util.Optional}.
     * 
     * @param uuid Objects uuid.
     * @return Optional
     */
    public Optional<Organization> findByUUID(UUID uuid);

    /**
     * Saves a given object to database. This will create a new one if it doesn't
     * exists.
     * 
     * 
     * @param organization Organization object.
     * @return created organization
     */
    public Organization create(Organization organization);
}
