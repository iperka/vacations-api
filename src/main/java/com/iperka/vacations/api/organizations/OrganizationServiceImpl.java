package com.iperka.vacations.api.organizations;

import java.util.Optional;
import java.util.UUID;

import com.iperka.vacations.api.organizations.exceptions.OrganizationAlreadyExists;
import com.iperka.vacations.api.organizations.exceptions.OrganizationNotFound;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * The
 * {@link com.iperka.vacations.api.organizations.OrganizationServiceImpl}
 * class implements the
 * {@link com.iperka.vacations.api.organizations.OrganizationService} interface
 * and is used to manage the organization.
 * 
 * @author Michael Beutler
 * @version 0.0.6
 * @since 2021-09-29
 */
@Service
@Slf4j
public class OrganizationServiceImpl implements OrganizationService {

    private OrganizationRepository organizationRepository;

    public OrganizationServiceImpl(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @Override
    @PreAuthorize("hasAuthority('SCOPE_organizations:all:read')")
    public Page<Organization> findAll(Pageable pageable) {
        log.debug("findAll called");
        return this.organizationRepository.findAll(pageable);
    }

    @Override
    @PreAuthorize("hasAuthority('SCOPE_organizations:all:read')")
    public Page<Organization> findByNameContainingIgnoreCase(Pageable pageable, String name) {
        log.debug("findAllByName called");
        return this.organizationRepository.findByNameContainingIgnoreCase(pageable, name);
    }

    @Override
    @PreAuthorize("hasAuthority('SCOPE_organizations:all:read')")
    public Organization findByUuid(UUID uuid) throws OrganizationNotFound {
        log.debug("findByUUID called");
        return this.organizationRepository.findByUuid(uuid).orElseThrow(() -> new OrganizationNotFound());
    }

    @Override
    @PreAuthorize("hasAuthority('SCOPE_organizations:write')")
    public Organization create(Organization organization) throws OrganizationAlreadyExists {
        log.debug("create called");

        if (this.findByNameIgnoreCase(organization.getName()).isPresent()) {
            throw new OrganizationAlreadyExists();
        }

        return this.organizationRepository.save(organization);
    }

    @Override
    @PreAuthorize("hasAuthority('SCOPE_organizations:all:read')")
    public Optional<Organization> findByNameIgnoreCase(String name) {
        log.debug("findByNameIgnoreCase called");
        return this.organizationRepository.findByNameIgnoreCase(name);
    }

    @Override
    @PreAuthorize("hasAuthority('SCOPE_organizations:all:write')")
    public void deleteByUuid(UUID uuid) {
        this.organizationRepository.deleteByUuid(uuid);
    }

    @Override
    @PreAuthorize("hasAuthority('SCOPE_organizations:read')")
    public Page<Organization> findAllByOwner(Pageable pageable, String owner) {
        return this.organizationRepository.findAllByOwner(pageable, owner);
    }

    @Override
    @PreAuthorize("hasAuthority('SCOPE_organizations:read')")
    public Page<Organization> findByNameContainingIgnoreCaseAndOwner(Pageable pageable, String name, String owner) {
        return this.organizationRepository.findByNameContainingIgnoreCaseAndOwner(pageable, name, owner);
    }

    @Override
    @PreAuthorize("hasAuthority('SCOPE_organizations:read')")
    public Organization findByUuidAndOwner(UUID uuid, String owner) throws OrganizationNotFound {
        return this.organizationRepository.findByUuidAndOwner(uuid, owner)
                .orElseThrow(() -> new OrganizationNotFound());
    }

    @Override
    @PreAuthorize("hasAuthority('SCOPE_organizations:read')")
    public Optional<Organization> findByNameIgnoreCaseAndOwner(String name, String owner) {
        return this.organizationRepository.findByNameIgnoreCaseAndOwner(name, owner);
    }

    @Override
    @PreAuthorize("hasAuthority('SCOPE_organizations:write')")
    public void deleteByUuidAndOwner(UUID uuid, String owner) {
        this.organizationRepository.deleteByUuidAndOwner(uuid, owner);
    }
}
