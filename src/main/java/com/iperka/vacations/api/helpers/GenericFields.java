package com.iperka.vacations.api.helpers;



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
 * @version 1.0.1
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@MappedSuperclass
public abstract class GenericFields extends AuditMetadata {
    /**
     * Unique identifier for model. This field must
     * not be changed after creation.
     */
    @Id
    @GeneratedValue
    @Column(nullable = false, unique = true)
    @Schema(description = "Unique identifier for object.", example = "3069422c032-1652891906326", required = true)
    private String id;

    /**
     * Defines the resource owner for this object.
     */
    @Column(nullable = false)
    @NotNull
    @Schema(description = "Owner of the object.", example = "iperka|2dd222awd2", required = true)
    private String owner;
}
