package com.iperka.vacations.api.organizations;

import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import com.iperka.vacations.api.organizations.dto.OrganizationDTO;
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
 * @version 0.0.9
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
    @PreAuthorize("hasAnyAuthority('SCOPE_organizations:all:read', 'SCOPE_organizations:all:write')")
    public Page<Organization> findAll(Pageable pageable) {
        log.debug("findAll called");
        return this.organizationRepository.findAll(pageable);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('SCOPE_organizations:all:read', 'SCOPE_organizations:all:write')")
    public Page<Organization> findByNameContainingIgnoreCase(Pageable pageable, String name) {
        log.debug("findAllByName called");
        return this.organizationRepository.findByNameContainingIgnoreCase(pageable, name);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('SCOPE_organizations:all:read', 'SCOPE_organizations:all:write')")
    public Organization findByUuid(UUID uuid) throws OrganizationNotFound {
        log.debug("findByUUID called");
        return this.organizationRepository.findByUuid(uuid).orElseThrow(OrganizationNotFound::new);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('SCOPE_organizations:write', 'SCOPE_organizations:all:write')")
    public Organization create(Organization organization) throws OrganizationAlreadyExists {
        log.debug("create called");

        if (this.findByNameIgnoreCase(organization.getName()).isPresent()) {
            throw new OrganizationAlreadyExists();
        }

        return this.organizationRepository.save(organization);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('SCOPE_organizations:all:read', 'SCOPE_organizations:all:write')")
    public Optional<Organization> findByNameIgnoreCase(String name) {
        log.debug("findByNameIgnoreCase called");
        return this.organizationRepository.findByNameIgnoreCase(name);
    }

    @Override
    @PreAuthorize("hasAuthority('SCOPE_organizations:all:write')")
    @Transactional
    public void deleteByUuid(UUID uuid) {
        this.organizationRepository.deleteByUuid(uuid);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('SCOPE_organizations:read', 'SCOPE_organizations:write', 'SCOPE_organizations:all:read', 'SCOPE_organizations:all:write')")
    public Page<Organization> findAllByOwner(Pageable pageable, String owner) {
        return this.organizationRepository.findAllByOwner(pageable, owner);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('SCOPE_organizations:read', 'SCOPE_organizations:write', 'SCOPE_organizations:all:read', 'SCOPE_organizations:all:write')")
    public Page<Organization> findByNameContainingIgnoreCaseAndOwner(Pageable pageable, String name, String owner) {
        return this.organizationRepository.findByNameContainingIgnoreCaseAndOwner(pageable, name, owner);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('SCOPE_organizations:read', 'SCOPE_organizations:write', 'SCOPE_organizations:all:read', 'SCOPE_organizations:all:write')")
    public Organization findByUuidAndOwner(UUID uuid, String owner) throws OrganizationNotFound {
        return this.organizationRepository.findByUuidAndOwner(uuid, owner)
                .orElseThrow(OrganizationNotFound::new);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('SCOPE_organizations:read', 'SCOPE_organizations:write', 'SCOPE_organizations:all:read', 'SCOPE_organizations:all:write')")
    public Optional<Organization> findByNameIgnoreCaseAndOwner(String name, String owner) {
        return this.organizationRepository.findByNameIgnoreCaseAndOwner(name, owner);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('SCOPE_organizations:write', 'SCOPE_organizations:all:write')")
    @Transactional
    public void deleteByUuidAndOwner(UUID uuid, String owner) {
        this.organizationRepository.deleteByUuidAndOwner(uuid, owner);
    }

    @Override
    @PreAuthorize("hasAuthority('SCOPE_organizations:all:write')")
    public Organization updateByUuid(UUID uuid, OrganizationDTO organizationDTO)
            throws OrganizationNotFound, OrganizationAlreadyExists {
        Organization organization = this.organizationRepository.findByUuid(uuid).orElseThrow(OrganizationNotFound::new);

        if (!organizationDTO.getName().equals(organization.getName())) {
            // name has been modified -> check if name is unique
            if (this.findByNameIgnoreCase(organizationDTO.getName()).isPresent()) {
                throw new OrganizationAlreadyExists();
            }
            organization.setName(organizationDTO.getName());
        }

        // Should only be enabled by higher priviliged principal
        if (organizationDTO.isEnabled() != organization.isEnabled()) {
            organization.setEnabled(organizationDTO.isEnabled());
        }

        return this.organizationRepository.save(organization);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('SCOPE_organizations:write', 'SCOPE_organizations:all:write')")
    public Organization updateByUuidAndOwner(UUID uuid, String owner, OrganizationDTO organizationDTO)
            throws OrganizationNotFound, OrganizationAlreadyExists {
        Organization organization = this.organizationRepository.findByUuidAndOwner(uuid, owner)
                .orElseThrow(OrganizationNotFound::new);

        if (!organizationDTO.getName().equals(organization.getName())) {
            // name has been modified -> check if name is unique
            if (this.findByNameIgnoreCase(organizationDTO.getName()).isPresent()) {
                throw new OrganizationAlreadyExists();
            }
            organization.setName(organizationDTO.getName());
        }

        // Should only be enabled by higher priviliged principal
        if (organizationDTO.isEnabled() != organization.isEnabled()) {
            log.warn("Unprivileged principal tried to setEnabled!");
        }

        return this.organizationRepository.save(organization);
    }
}
