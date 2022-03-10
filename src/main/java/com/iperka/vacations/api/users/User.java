package com.iperka.vacations.api.users;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.iperka.vacations.api.helpers.GenericFields;
import com.iperka.vacations.api.helpers.Ownable;

import org.hibernate.validator.constraints.Length;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * The {@link com.iperka.vacations.api.users.User} class defines
 * the structure of a basic user. This object is only used for friendship
 * suggestions.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 1.0.9
 */
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "uuid" })
})
@Data
public class User extends GenericFields implements Ownable {
    @Column(nullable = false, length = 500)
    @Length(min = 25, max = 500)
    @NotNull
    @Schema(description = "Hash of user email.", example = "78e731027d8fd50ed642340b7c9a63b3", required = true)
    private String emailHash;

    @Column(nullable = false, length = 500)
    @Length(min = 25, max = 500)
    @NotNull
    @Schema(description = "Hash of user phone.", example = "78e731027d8fd50ed642340b7c9a63b3", required = true)
    private String phoneHash;
}
