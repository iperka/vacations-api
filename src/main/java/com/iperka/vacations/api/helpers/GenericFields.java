package com.iperka.vacations.api.helpers;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import com.iperka.vacations.api.security.AuditMetadata;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * For less duplicated line, objects can extend this class.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@MappedSuperclass
public abstract class GenericFields extends AuditMetadata {
    /**
     * Unique identifier for model. Uses the
     * built in {@link java.util.UUID} object to generate UUIDs. In the database
     * schema the field is of the type {@code BINARY(16) NOT NULL}. This field must
     * not be changed after creation.
     */
    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)", nullable = false, unique = true)
    @Schema(description = "Unique identifier for object.", example = "67394e83-1ea5-495e-adf3-80ee93514f92", required = true, format = "uuid")
    private UUID uuid;

    /**
     * Defines the resource owner for this object.
     */
    @Column(nullable = false)
    @NotNull
    @Schema(description = "Owner of the object.", example = "iperka|2dd222awd2", required = true)
    private String owner;
}
