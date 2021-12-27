package com.iperka.vacations.api.organizations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Optional;
import java.util.UUID;

import com.iperka.vacations.api.organizations.dto.OrganizationDTO;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class OrganizationRepositoryTest {
	@Autowired
	private OrganizationRepository organizationRepository;

	@Test
	void shouldFindOrganizationByName() {
		OrganizationDTO organizationDTO = new OrganizationDTO();
		organizationDTO.setName("Test");
		Organization organizationBeforeSave = organizationDTO.toObject();
		organizationBeforeSave.setOwner("me");

		Organization organization = organizationRepository.save(organizationBeforeSave);

		Optional<Organization> result = organizationRepository.findByNameIgnoreCase("test");
		assertEquals(true, result.isPresent());
		if (result.isPresent()) {
			assertEquals(true, result.get().getUuid().compareTo(organization.getUuid()) == 0);
		}

		assertFalse(organizationRepository.findByNameIgnoreCase("invalidName").isPresent());
	}

	@Test
	void shouldFindOrganizationById() {
		OrganizationDTO organizationDTO = new OrganizationDTO();
		organizationDTO.setName("Test");
		Organization organizationBeforeSave = organizationDTO.toObject();
		organizationBeforeSave.setOwner("me");

		Organization organization = organizationRepository.save(organizationBeforeSave);

		Optional<Organization> result = organizationRepository.findById(organization.getUuid());
		assertEquals(true, result.isPresent());
		if (result.isPresent()) {
			assertEquals(true, result.get().getUuid().compareTo(organization.getUuid()) == 0);
		}

		assertFalse(organizationRepository.findById(UUID.randomUUID()).isPresent());
	}
}
