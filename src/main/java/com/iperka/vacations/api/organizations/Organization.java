package com.iperka.vacations.api.organizations;

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
 * The {@link com.iperka.vacations.api.organizations.Organization} class defines
 * the structure of a basic organization.
 * 
 * @author Michael Beutler
 * @version 0.0.7
 * @since 2021-09-29
 */
@Entity
@Table(name = "organizations", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "uuid", "name" })
})
@Data
public class Organization implements Ownable {
    /**
     * Unique identifier for
     * {@link com.iperka.vacations.api.organizations.Organization} model. Uses the
     * built in {@link java.util.UUID} object to generate UUIDs. In the database
     * schema the field is of the type {@code BINARY(16) NOT NULL}. This field must
     * not be changed after creation.
     */
    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)", nullable = false, unique = true)
    @Schema(description = "Unique identifier of the Organization.", example = "67394e83-1ea5-495e-adf3-80ee93514f92", required = true, format = "uuid")
    private UUID uuid;

    /**
     * Unique string used for naming the organization.
     */
    @Column(unique = true, nullable = false, length = 100)
    @Length(min = 4, max = 100)
    @NotNull
    @Schema(description = "Unique human readable identifier of the Organization.", example = "iperka", required = true)
    private String name;

    /**
     * Will be false if the organization is marked as disabled. As default every
     * organization is disabled and needs to be enabled in order to function as
     * authorization object.
     */
    @Column(nullable = false)
    @NotNull
    @Schema(description = "Defines if the Organization is enabled or not.", example = "true", required = true)
    private boolean enabled = false;

    /**
     * Defines the resource owner for this object.
     */
    @Column(nullable = false)
    @NotNull
    @Schema(description = "Owner of the Organzitation.", example = "iperka@example.com|2dawd2", required = true)
    private String owner;

    /**
     * Stores the date time when the organization object has been created.
     */
    @Column(nullable = false)
    @NotNull
    private Date created = new Date();

    /**
     * Stores the date time when the organization object has been updated.
     */
    @Column(nullable = false)
    @NotNull
    private Date updated = new Date();

    /**
     * Set updated field to current date. This method gets triggered every time the
     * organization object changes.
     */
    @PreUpdate
    public void setLastUpdate() {
        this.updated = new Date();
    }
}
