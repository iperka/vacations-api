package com.iperka.vacations.api.vacations;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.iperka.vacations.api.helpers.GenericFields;
import com.iperka.vacations.api.helpers.Ownable;

import org.hibernate.validator.constraints.Length;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * The {@link com.iperka.vacations.api.vacations.Vacation} class defines
 * the structure of a basic vacation.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 2021-12-28
 */
@Entity
@Table(name = "vacations", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "uuid" })
})
@Data
public class Vacation extends GenericFields implements Ownable {
    /**
     * Unique string used for naming the vacation.
     */
    @Column(nullable = false, length = 100)
    @Length(min = 4, max = 100)
    @NotNull
    @Schema(description = "Unique human readable identifier of the Vacation.", example = "Ski Trip", required = true)
    private String name;

    /**
     * Start date of the vacations.
     */
    @Column(nullable = false)
    @NotNull
    @Schema(description = "Start date of Vacation.", required = true)
    private Date startDate;

    /**
     * End date of the vacations.
     */
    @Column(nullable = false)
    @NotNull
    @Schema(description = "End date of Vacation.", required = true)
    private Date endDate;

    /**
     * Number of days required for the Vacation.
     */
    @Column(nullable = false)
    @NotNull
    @Schema(description = "Number of vacation days required.", required = true)
    private double days;

    /**
     * Vacation status.
     */
    @Column(nullable = false)
    @NotNull
    @Schema(description = "Defines the Vacation state.", example = "requested", required = true)
    @Enumerated(EnumType.STRING)
    private VacationStatus status = VacationStatus.REQUESTED;

    public String getStatus() {
        return this.status.toString().toLowerCase();
    }

    /**
     * Vacation type.
     */
    @Column(nullable = false)
    @NotNull
    @Schema(description = "Defines the Vacation tpye.", example = "private", required = true)
    @Enumerated(EnumType.STRING)
    private VacationType type = VacationType.PRIVATE;

    public String getType() {
        return this.type.toString().toLowerCase();
    }
}
