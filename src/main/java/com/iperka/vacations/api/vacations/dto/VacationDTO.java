package com.iperka.vacations.api.vacations.dto;

import javax.validation.constraints.NotNull;

import com.iperka.vacations.api.helpers.DTO;
import com.iperka.vacations.api.vacations.Vacation;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VacationDTO implements DTO<Vacation> {

    @Length(min = 4, max = 100)
    @NotNull
    private String name;

    @NotNull
    private boolean enabled;

    @Override
    public Vacation toObject() {
        final Vacation vacation = new Vacation();

        vacation.setName(this.name);
        vacation.setEnabled(this.enabled);

        return vacation;
    }

}
