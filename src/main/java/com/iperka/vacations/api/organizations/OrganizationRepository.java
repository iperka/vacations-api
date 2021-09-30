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
 * @version 0.0.3
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
     * Returns the user object matching the given uuid as
     * {@link java.util.Optional}.
     * 
     * @param uuid Organization uuid.
     * @return Optional
     */
    public Optional<Organization> findById(UUID uuid);

    /**
     * Returns the user object matching the given name as
     * {@link java.util.Optional}.
     * 
     * @param name Organization name.
     * @return Optional
     */
    public Optional<Organization> findByNameIgnoreCase(String name);

    /**
     * Returns the organization objects matching the given name as
     * {@link java.util.Page}.
     * 
     * @param name Organization name.
     * @return Page
     */
    public Page<Organization> findByNameContainingIgnoreCase(Pageable pageable, String name);
}
