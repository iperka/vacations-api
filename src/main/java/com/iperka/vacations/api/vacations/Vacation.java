package com.iperka.vacations.api.vacations;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.iperka.vacations.api.helpers.Ownable;

import org.hibernate.validator.constraints.Length;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * The {@link com.iperka.vacations.api.vacations.Vacation} class defines
 * the structure of a basic vacation.
 * 
 * @author Michael Beutler
 * @version 0.0.3
 * @since 2021-12-28
 */
@Entity
@Table(name = "vacations", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "uuid" })
})
@Data
public class Vacation implements Ownable {
    /**
     * Unique identifier for
     * {@link com.iperka.vacations.api.vacations.Vacation} model. Uses the
     * built in {@link java.util.UUID} object to generate UUIDs. In the database
     * schema the field is of the type {@code BINARY(16) NOT NULL}. This field must
     * not be changed after creation.
     */
    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)", nullable = false, unique = true)
    @Schema(description = "Unique identifier of the Vacation.", example = "67394e83-1ea5-495e-adf3-80ee93514f92", required = true, format = "uuid")
    private UUID uuid;

    /**
     * Unique string used for naming the vacation.
     */
    @Column(unique = true, nullable = false, length = 100)
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
     * Will be false if the vacation is marked as disabled. As default every
     * vacation is disabled and needs to be enabled in order to function as
     * authorization object.
     */
    @Column(nullable = false)
    @NotNull
    @Schema(description = "Defines if the Vacation is enabled or not.", example = "true", required = true)
    private boolean enabled = false;

    /**
     * Defines the resource owner for this object.
     */
    @Column(nullable = false)
    @NotNull
    @Schema(description = "Owner of the Organzitation.", example = "iperka|2dd222awd2", required = true)
    private String owner;

    /**
     * Stores the date time when the vacation object has been created.
     */
    @Column(nullable = false)
    @NotNull
    private Date created = new Date();

    /**
     * Stores the date time when the vacation object has been updated.
     */
    @Column(nullable = false)
    @NotNull
    private Date updated = new Date();

    /**
     * Set updated field to current date. This method gets triggered every time the
     * vacation object changes.
     */
    @PreUpdate
    public void setLastUpdate() {
        this.updated = new Date();
    }
}
