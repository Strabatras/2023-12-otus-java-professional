package ru.otus.enums;

public enum Room {
    MUTE_ROOM("1408"),
    AGGREGATE_ROOM("1408")
    ;
    private String criterion;

    Room(String criterion) {
        this.criterion = criterion;
    }

    public String getCriterion() {
        return criterion;
    }
}