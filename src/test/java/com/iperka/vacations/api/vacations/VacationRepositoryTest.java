package com.iperka.vacations.api.vacations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import com.iperka.vacations.api.vacations.dto.VacationDTO;

@DataMongoTest
class VacationRepositoryTest {
	@Autowired
	private VacationRepository vacationRepository;

	@Test
	void shouldFindVacationByUuid() {
		VacationDTO vacationDTO = new VacationDTO();
		Vacation vacationBeforeSave = vacationDTO.toObject();
		vacationBeforeSave.setOwner("me");
		vacationBeforeSave.setTitle("test");
		vacationBeforeSave.setStartDate(new Date());
		vacationBeforeSave.setEndDate(new Date());

		Vacation vacation = vacationRepository.save(vacationBeforeSave);

		Optional<Vacation> result = vacationRepository.findByUuid(vacation.getUuid());
		assertEquals(true, result.isPresent());
		if (result.isPresent()) {
			assertEquals(true, result.get().getUuid().compareTo(vacation.getUuid()) == 0);
		}

		assertFalse(vacationRepository.findByUuid(UUID.randomUUID()).isPresent());
	}
}