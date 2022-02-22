package com.iperka.vacations.api.helpers;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * DateCalculator class for helping with different date operations.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 1.0.0
 */
public class DateCalculator {

    /**
     * Get business dates for given date range.
     * 
     * @param startDate start date.
     * @param endDate   end date.
     * @param holidays  list of special holidays.
     * @return List of dates.
     */
    public static List<LocalDate> getBusinessDaysBetween(final LocalDate startDate,
            LocalDate endDate,
            final Optional<List<LocalDate>> holidays) throws IllegalArgumentException {
        // Validate method arguments

        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(
                    "Invalid method argument(s) to countBusinessDaysBetween (" + startDate
                            + "," + endDate + "," + holidays + ")");
        }

        // Predicate 1: Is a given date is a holiday
        Predicate<LocalDate> isHoliday = date -> holidays.isPresent()
                && holidays.get().contains(date);

        // Predicate 2: Is a given date is a weekday
        Predicate<LocalDate> isWeekend = date -> date.getDayOfWeek() == DayOfWeek.SATURDAY
                || date.getDayOfWeek() == DayOfWeek.SUNDAY;

        // Iterate over stream of all dates and check each day against any weekday or
        // holiday
        return startDate.datesUntil(endDate)
                .filter(isWeekend.or(isHoliday).negate())
                .collect(Collectors.toList());
    }

    /**
     * Get count of business days for given date range.
     * 
     * @param startDate start date.
     * @param endDate   end date.
     * @param holidays  list of special holidays.
     * @return count of business.
     */
    public static int countBusinessDaysBetween(final LocalDate startDate,
            final LocalDate endDate,
            final Optional<List<LocalDate>> holidays) throws IllegalArgumentException {
        List<LocalDate> businessDays = DateCalculator.getBusinessDaysBetween(startDate, endDate, holidays);

        return businessDays.size();
    }

    private DateCalculator() {
    }

}
