package com.iperka.vacations.api.vacations;

public enum VacationType {
    PUBLIC("public"),
    PRIVATE("private");

    private final String type;

    private VacationType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
