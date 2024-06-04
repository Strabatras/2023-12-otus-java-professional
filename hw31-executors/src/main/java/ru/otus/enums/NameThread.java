package ru.otus.enums;

public enum NameThread {
    ONE ("Первый поток"),
    TWO("Второй поток");

    private String name;

    NameThread(String name) {
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}
