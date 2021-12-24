package com.iperka.vacations.api.helpers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DateCalculatorTest {
    @Test
    void shouldReturnCorrectCountOfDates() {
        assertEquals(DateCalculator.countBusinessDaysBetween(LocalDate.now(), LocalDate.now(),
                Optional.empty()), 0);

        assertEquals(DateCalculator.countBusinessDaysBetween(LocalDate.of(2021, 12, 20), LocalDate.of(2021, 12, 26),
                Optional.empty()), 5);

        assertEquals(DateCalculator.countBusinessDaysBetween(LocalDate.of(2021, 12, 20), LocalDate.of(2021, 12, 26),
                Optional.of(List.of(LocalDate.of(2021, 12, 24)))), 4);
    }

    @Test
    void shouldReturnCorrectDates() {
        assertEquals(DateCalculator.getBusinessDaysBetween(LocalDate.of(2021, 12, 20), LocalDate.of(2021, 12, 26),
                Optional.of(List.of(LocalDate.of(2021, 12, 24)))),
                List.of(
                        LocalDate.of(2021, 12, 20),
                        LocalDate.of(2021, 12, 21),
                        LocalDate.of(2021, 12, 22),
                        LocalDate.of(2021, 12, 23)));
    }

    @Test
    void shouldThrowIfInvalidDatesProvides() {
        assertThrows(IllegalArgumentException.class,
                () -> DateCalculator.countBusinessDaysBetween(LocalDate.of(2021, 12, 20), LocalDate.of(2021, 12, 19),
                        Optional.empty()));
    }
}
