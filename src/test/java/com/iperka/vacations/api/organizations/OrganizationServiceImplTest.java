package com.iperka.vacations.api.organizations;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrganizationServiceImplTest {
    @Mock
    private OrganizationRepository organizationRepository;

    OrganizationServiceImpl organizationService;

    @BeforeEach
    void initUseCase() {
        organizationService = new OrganizationServiceImpl(organizationRepository);
    }

    @Test
    void createOrganization() {
        Organization organization = new Organization();
        organization.setName("Test");

        // providing knowledge
        when(organizationRepository.save(any(Organization.class))).thenReturn(organization);

        Organization savedCustomer = organizationRepository.save(organization);
        assertNotNull(savedCustomer.getName());
    }

}