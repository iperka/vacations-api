package com.iperka.vacations.api.friendships.dto;

import javax.validation.constraints.NotNull;

import com.iperka.vacations.api.friendships.Friendship;
import com.iperka.vacations.api.friendships.FriendshipStatus;
import com.iperka.vacations.api.helpers.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendshipDTO implements DTO<Friendship> {
    @NotNull
    private String user;

    @NotNull
    private FriendshipStatus status = FriendshipStatus.ACCEPTED;

    @Override
    public Friendship toObject() {
        final Friendship friendship = new Friendship();
        friendship.setUser(user);
        friendship.setStatus(status);

        return friendship;
    }

    @Schema(description = "User for friendship object.", example = "iperka|2dd222awd2", required = true)
    public void setUser(String user) {
        this.user = user;
    }

    @Schema(description = "Defines the Friendship status.", example = "accepted", required = true)
    public void setStatus(String status) {
        this.status = FriendshipStatus.valueOf(status.toUpperCase());
    }
}
