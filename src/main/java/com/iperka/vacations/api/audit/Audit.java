package com.iperka.vacations.api.audit;

import java.util.Map;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.iperka.vacations.api.helpers.GenericFields;
import com.vladmihalcea.hibernate.type.json.JsonType;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Audit POJO class for all models.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "audits", uniqueConstraints = { @UniqueConstraint(columnNames = { "id" }) })
@AllArgsConstructor
@NoArgsConstructor
@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonType.class)
})
@Document(collection = "audits")
public class Audit extends GenericFields {
    @NonNull
    @Schema(example = "com.iperka.vacations.api.Example")
    private String objectType;

    @NonNull
    @Column(columnDefinition = "BINARY(16)", nullable = false, unique = true)
    @Schema(description = "Unique identifier for object.", example = "3069422c032-1652891906326", required = true)
    private String objectId;

    @NonNull
    @Schema(example = "created")
    private AuditOperation operation;

    @Schema(example = "null")
    @Type(type = "json")
    @Column(columnDefinition = "json")
    private Map<String, Map<String, String>> diff;

    @Schema(example = "Created com.iperka.vacations.api.Example with id 3069422c032-1652891906326.")
    private String description;

    public String getOperation() {
        return this.operation.toString().toLowerCase();
    }
}
