package com.iperka.vacations.api.organizations;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * The {@link com.iperka.vacations.api.organizations.OrganizationServiceImpl}
 * class implements the
 * {@link com.iperka.vacations.api.organizations.OrganizationService} interface
 * and is used to manage the organization.
 * 
 * @author Michael Beutler
 * @version 0.0.3
 * @since 2021-09-29
 */
@Service
public class OrganizationServiceImpl implements OrganizationService {
    private final Logger logger = LoggerFactory.getLogger(OrganizationServiceImpl.class);

    @Autowired
    private OrganizationRepository organizationRepository;

    @Override
    public Page<Organization> findAll(Pageable pageable) {
        logger.debug("findAll called");
        return this.organizationRepository.findAll(pageable);
    }

    @Override
    public Page<Organization> findByNameContainingIgnoreCase(Pageable pageable, String name) {
        logger.debug("findAllByName called");
        return this.organizationRepository.findByNameContainingIgnoreCase(pageable, name);
    }

    @Override
    public Optional<Organization> findByUUID(UUID uuid) {
        logger.debug("findByUUID called");
        return this.organizationRepository.findById(uuid);
    }

    @Override
    public Organization create(Organization organization) {
        logger.debug("create called");

        if (this.findByNameIgnoreCase(organization.getName()).isPresent()) {
            // TODO: Throw custom error for duplicate name
            return null;
        }

        return this.organizationRepository.save(organization);
    }

    @Override
    public Optional<Organization> findByNameIgnoreCase(String name) {
        logger.debug("findByNameIgnoreCase called");
        return this.organizationRepository.findByNameIgnoreCase(name);
    }
}
