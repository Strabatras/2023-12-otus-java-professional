package ru.otus.enums;

public enum Nominal {
    FIVE(5),
    THEN(10),
    FIFTY(50),
    ONE_HUNDRED(100),
    TWO_HUNDRED(200),
    FIVE_HUNDRED(500),
    THOUSAND(1000),
    TWO_THOUSAND(2000),
    FIVE_THOUSAND(5000);

    private int dignity;

    Nominal(int dignity) {
        this.dignity = dignity;
    }

    public int getDignity() {
        return dignity;
    }
}
