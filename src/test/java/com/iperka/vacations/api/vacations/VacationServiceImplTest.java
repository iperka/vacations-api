package com.iperka.vacations.api.vacations;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VacationServiceImplTest {
    @Mock
    private VacationRepository vacationRepository;

    VacationServiceImpl vacationService;

    @BeforeEach
    void initUseCase() {
        vacationService = new VacationServiceImpl(vacationRepository);
    }

    @Test
    void createVacation() {
        Vacation vacation = new Vacation();
        vacation.setName("Test");
        vacation.setStartDate(new Date());
		vacation.setEndDate(new Date());

        // providing knowledge
        when(vacationRepository.save(any(Vacation.class))).thenReturn(vacation);

        Vacation savedCustomer = vacationRepository.save(vacation);
        assertNotNull(savedCustomer.getName());
    }

}