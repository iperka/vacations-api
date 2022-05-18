package com.iperka.vacations.api.vacations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Date;
import java.util.Optional;

import com.iperka.vacations.api.vacations.dto.VacationDTO;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

@DataMongoTest
class VacationRepositoryTest {
	@Autowired
	private VacationRepository vacationRepository;

	@Test
	void shouldFindVacationById() {
		VacationDTO vacationDTO = new VacationDTO();
		Vacation vacationBeforeSave = vacationDTO.toObject();
		vacationBeforeSave.setOwner("me");
		vacationBeforeSave.setTitle("test");
		vacationBeforeSave.setStartDate(new Date());
		vacationBeforeSave.setEndDate(new Date());

		Vacation vacation = vacationRepository.save(vacationBeforeSave);

		Optional<Vacation> result = vacationRepository.findById(vacation.getId());
		assertEquals(true, result.isPresent());
		if (result.isPresent()) {
			assertEquals(true, result.get().getId().compareTo(vacation.getId()) == 0);
		}

		assertFalse(vacationRepository.findById("invalidId").isPresent());
	}
}