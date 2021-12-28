package com.iperka.vacations.api.vacations.dto;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import com.iperka.vacations.api.helpers.DTO;
import com.iperka.vacations.api.helpers.DateCalculator;
import com.iperka.vacations.api.vacations.Vacation;
import com.iperka.vacations.api.vacations.VacationStatus;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class VacationDTO implements DTO<Vacation> {

    @Length(min = 4, max = 100)
    @NotNull
    private String name;

    @NotNull
    private Date startDate;

    @NotNull
    private Date endDate;

    @NotNull
    private double days = -1;

    @NotNull
    private VacationStatus status = VacationStatus.REQUESTED;

    @Override
    public Vacation toObject() {
        final Vacation vacation = new Vacation();

        vacation.setName(this.name);
        vacation.setStartDate(this.startDate);
        vacation.setEndDate(this.endDate);

        if (this.days < 0) {
            try {
                LocalDate startDateAsLocal = LocalDate.ofInstant(this.startDate.toInstant(), ZoneId.systemDefault());
                LocalDate endDateAsLocal = LocalDate.ofInstant(this.endDate.toInstant(), ZoneId.systemDefault());
                this.days = DateCalculator.countBusinessDaysBetween(startDateAsLocal, endDateAsLocal, Optional.empty());
            } catch (Exception e) {
                log.error("Exception occurred while calculating days.", e);
            }

        }

        vacation.setDays(this.days);
        vacation.setStatus(this.status);

        return vacation;
    }

    public void setStatus(String status) {
        this.status = VacationStatus.valueOf(status.toUpperCase());
    }
}
