package com.iperka.vacations.api.vacations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import com.iperka.vacations.api.vacations.dto.VacationDTO;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class VacationRepositoryTest {
	@Autowired
	private VacationRepository vacationRepository;

	@Test
	void shouldFindVacationByName() {
		VacationDTO vacationDTO = new VacationDTO();
		vacationDTO.setName("Test");
		vacationDTO.setStartDate(new Date());
		vacationDTO.setEndDate(new Date());
		Vacation vacationBeforeSave = vacationDTO.toObject();
		vacationBeforeSave.setOwner("me");

		Vacation vacation = vacationRepository.save(vacationBeforeSave);

		Optional<Vacation> result = vacationRepository.findByNameIgnoreCase("test");
		assertEquals(true, result.isPresent());
		if (result.isPresent()) {
			assertEquals(true, result.get().getUuid().compareTo(vacation.getUuid()) == 0);
		}

		assertFalse(vacationRepository.findByNameIgnoreCase("invalidName").isPresent());
	}

	@Test
	void shouldFindVacationById() {
		VacationDTO vacationDTO = new VacationDTO();
		vacationDTO.setName("Test");
		vacationDTO.setStartDate(new Date());
		vacationDTO.setEndDate(new Date());
		Vacation vacationBeforeSave = vacationDTO.toObject();
		vacationBeforeSave.setOwner("me");

		Vacation vacation = vacationRepository.save(vacationBeforeSave);

		Optional<Vacation> result = vacationRepository.findById(vacation.getUuid());
		assertEquals(true, result.isPresent());
		if (result.isPresent()) {
			assertEquals(true, result.get().getUuid().compareTo(vacation.getUuid()) == 0);
		}

		assertFalse(vacationRepository.findById(UUID.randomUUID()).isPresent());
	}
}
