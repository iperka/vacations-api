package com.iperka.vacations.api.helpers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DateCalculatorTest {
    @Test
    void shouldReturnCorrectCountOfDates() {
        assertEquals(1, DateCalculator.countBusinessDaysBetween(LocalDate.of(2022,2,23), LocalDate.of(2022,2,23),
                Optional.empty()));

        assertEquals(5, DateCalculator.countBusinessDaysBetween(LocalDate.of(2021, 12, 20),
                LocalDate.of(2021, 12, 26),
                Optional.empty()));

        assertEquals(4, DateCalculator.countBusinessDaysBetween(LocalDate.of(2021, 12, 20),
                LocalDate.of(2021, 12, 26),
                Optional.of(List.of(LocalDate.of(2021, 12, 24)))));
    }

    @Test
    void shouldReturnCorrectDates() {
        assertEquals(List.of(
                LocalDate.of(2021, 12, 20),
                LocalDate.of(2021, 12, 21),
                LocalDate.of(2021, 12, 22),
                LocalDate.of(2021, 12, 23)),
                DateCalculator.getBusinessDaysBetween(LocalDate.of(2021, 12, 20),
                        LocalDate.of(2021, 12, 26),
                        Optional.of(List.of(LocalDate.of(2021, 12, 24)))));
    }

    @Test
    void shouldThrowIfInvalidDatesProvides() {
        assertThrows(IllegalArgumentException.class,
                () -> {
                    DateCalculator.countBusinessDaysBetween(LocalDate.of(2021, 12, 20),
                            LocalDate.of(2021, 12, 19),
                            Optional.empty());
                });

        assertThrows(IllegalArgumentException.class,
                () -> {
                    DateCalculator.countBusinessDaysBetween(null,
                            LocalDate.of(2021, 12, 19),
                            Optional.empty());
                });
    }
}
