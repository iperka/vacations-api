package com.iperka.vacations.api.organizations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.UUID;

import com.iperka.vacations.api.organizations.dto.OrganizationDTO;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class OrganizationRepositoryTest {
	@Autowired
	private OrganizationRepository organizationRepository;

	@Test
	public void shouldFindOrganizationByName() {
		OrganizationDTO organizationDTO = new OrganizationDTO();
		organizationDTO.setName("Test");
		Organization organizationBeforeSave = organizationDTO.toObject();
		organizationBeforeSave.setOwner("me");

		Organization organization = organizationRepository.save(organizationBeforeSave);

		Optional<Organization> result = organizationRepository.findByNameIgnoreCase("test");
		assertTrue(result.isPresent());
		if (result.isPresent()) {
			assertTrue(result.get().getUuid().compareTo(organization.getUuid()) == 0);
		}

		assertFalse(organizationRepository.findByNameIgnoreCase("invalidName").isPresent());
	}

	@Test
	public void shouldFindOrganizationById() {
		OrganizationDTO organizationDTO = new OrganizationDTO();
		organizationDTO.setName("Test");
		Organization organizationBeforeSave = organizationDTO.toObject();
		organizationBeforeSave.setOwner("me");

		Organization organization = organizationRepository.save(organizationBeforeSave);

		Optional<Organization> result = organizationRepository.findById(organization.getUuid());
		assertTrue(result.isPresent());
		if (result.isPresent()) {
			assertTrue(result.get().getUuid().compareTo(organization.getUuid()) == 0);
		}

		assertFalse(organizationRepository.findById(UUID.randomUUID()).isPresent());
	}
}
