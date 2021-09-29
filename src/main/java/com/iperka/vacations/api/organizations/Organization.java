package com.iperka.vacations.api.organizations;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * The {@link com.iperka.vacations.api.organizations.Organization} class defines
 * the structure of a basic organisation.
 * 
 * @author Michael Beutler
 * @version 0.0.1
 * @since 2021-09-29
 */
@Entity
@Table(name = "organizations")
public class Organization {
    /**
     * Unique identifier for
     * {@link com.iperka.vacations.api.organizations.Organization} model. Uses the
     * built in {@link java.util.UUID} object to generate UUIDs. In the database
     * schema the field is of the type {@code BINARY(16) NOT NULL}. This field must
     * not be changed after creation.
     */
    @Id
    @GeneratedValue
    @NotNull
    @Column(columnDefinition = "BINARY(16)", nullable = false, unique = true)
    private UUID uuid;

    /**
     * Unique string used for naming the organization.
     */
    @Column(unique = true, nullable = false, length = 100)
    @Min(4)
    @Max(100)
    @NotNull
    private String name;

    /**
     * Will be false if the organization is marked as disabled. As default every
     * organization is disabled and needs to be enabled in order to function as
     * authorization object.
     */
    @Column(nullable = false)
    @NotNull
    private boolean enabled = false;

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

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(final Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(final Date updated) {
        this.updated = updated;
    }

}
