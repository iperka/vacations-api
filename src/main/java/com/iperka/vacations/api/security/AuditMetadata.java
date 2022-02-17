package com.iperka.vacations.api.security;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Higher Order class to be extended by any document. Adds Audit fields to class
 * and automatically populates its values.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@MappedSuperclass
public abstract class AuditMetadata {

    /**
     * Stores the date time when the vacation object has been created.
     */
    @Column(nullable = false)
    @NotNull
    @Schema(description = "Object has been created at.", required = true, example = "1641921152721")
    private Date createdAt = new Date();

    /**
     * Stores the date time when the vacation object has been updated.
     */
    @Column(nullable = false)
    @NotNull
    @Schema(description = "Object has been lastly updated at.", required = true, example = "1641921152721")
    private Date updatedAt = new Date();

    @Schema(description = "Object has been created by user with id.", example = "iperka|61d48d95c3f245006c2caff3", required = true)
    @CreatedBy
    private String createdBy;

    @Schema(description = "Object has been updated by user with id.", example = "iperka|61d48d95c3f245006c2caff3", required = true)
    @LastModifiedBy
    private String updatedBy;

    /**
     * Set updated field to current date. This method gets triggered every time the
     * vacation object changes.
     */
    @PreUpdate
    public void setLastUpdate() {
        this.updatedAt = new Date();
    }
}
