package com.iperka.vacations.api.friendships;

@Deprecated
public enum FriendshipStatus {
    ACCEPTED("accepted"),
    BLOCKED("blocked");

    private final String status;

    private FriendshipStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }
}
