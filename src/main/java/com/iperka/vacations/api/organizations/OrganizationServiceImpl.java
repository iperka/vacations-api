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
 * @version 0.0.1
 * @since 2021-09-29
 */
@Service
public class OrganizationServiceImpl implements OrganizationService {
    private final Logger logger = LoggerFactory.getLogger(OrganizationServiceImpl.class);

    @Autowired
    private OrganizationRepository organizationRepository;

    @Override
    public Page<Organization> findAll(Pageable pageable) {
        return this.organizationRepository.findAll(pageable);
    }

    @Override
    public Optional<Organization> findByUUID(UUID uuid) {
        return this.organizationRepository.findById(uuid);
    }
}
