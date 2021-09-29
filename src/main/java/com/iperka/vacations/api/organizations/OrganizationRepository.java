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
 * @version 0.0.1
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
     * Returns the organization object matching the given username as
     * {@link java.util.Optional}. This method will be used for authentication.
     * 
     * @param name Organization name.
     * @return Optional
     */
    public Optional<Organization> findByName(String name);

    /**
     * Saves given {@link com.iperka.vacations.api.users.User} object to datasource.
     * This method should not be used without DTO.
     */
    public <S extends Organization> S save(Organization organization);
}
