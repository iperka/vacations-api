package com.iperka.vacations.api.vacations;

public enum VacationStatus {

    REQUESTED("requested"),
    ACCEPTED("accepted"),
    WITHDRAWN("withdrawn"),
    REJECTED("rejected");

    private final String status;

    private VacationStatus(String status) {
        this.status = status.toUpperCase();
    }

    @Override
    public String toString() {
        return status;
    }
}
