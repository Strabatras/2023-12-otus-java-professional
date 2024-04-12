package ru.otus.enums;

public enum ClientsWebServerUri {
    LOGIN_FORM_PAGE("/login"),
    CLIENTS_LIST_PAGE("/clients"),
    CLIENTS_FORM_PAGE("/clients/form");

    private final String uri;

    ClientsWebServerUri(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }
}
