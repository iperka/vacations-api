package com.iperka.vacations.api.friendships;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.iperka.vacations.api.helpers.GenericFields;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * The {@link com.iperka.vacations.api.friendships.Friendship} class defines
 * the structure of a basic friendship.
 * 
 * @deprecated Concept of friendship is not used anymore.
 * @author Michael Beutler
 * @version 1.0.1
 * @since 1.0.5
 */
@Entity
@Table(name = "friendships", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "uuid" })
})
@Data
@Deprecated
public class Friendship extends GenericFields {
    /**
     * Defines the user connected with this object.
     */
    @Column(nullable = false)
    @NotNull
    @Schema(description = "User for friendship object.", example = "iperka|2dd222awd2", required = true)
    private String user;

    /**
     * Friendship type.
     */
    @Column(nullable = false)
    @NotNull
    @Schema(description = "Defines the Friendship status.", example = "accepted", required = true)
    @Enumerated(EnumType.STRING)
    private FriendshipStatus status = FriendshipStatus.ACCEPTED;

    public String getType() {
        return this.status.toString().toLowerCase();
    }
}
