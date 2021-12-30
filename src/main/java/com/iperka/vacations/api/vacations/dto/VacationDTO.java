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

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "Defines the name of the vacation.", nullable = false, example = "Summer Vacation")
    public void setName(String name) {
        this.name = name;
    }

    @Schema(description = "Defines the start of the vacation.", nullable = false, example = "2022-01-01")
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Schema(description = "Defines the end of the vacation.", nullable = false, example = "2022-01-03T13:00:00")
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Schema(description = "Defines the number of vacation days for the vacation. (If null the API tries to calculate.)", nullable = true, example = "1.5")
    public void setDays(double days) {
        this.days = days;
    }

    @Schema(description = "Defines the current status of the vacation.", nullable = false, example = "requested")
    public void setStatus(String status) {
        this.status = VacationStatus.valueOf(status.toUpperCase());
    }

}
