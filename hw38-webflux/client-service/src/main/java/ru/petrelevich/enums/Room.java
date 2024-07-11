package ru.petrelevich.enums;

public enum Room {
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