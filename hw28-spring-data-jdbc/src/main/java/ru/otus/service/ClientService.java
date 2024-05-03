package ru.otus.service;

import ru.otus.model.Client;
import ru.otus.request.ClientRequest;

import java.util.List;

public interface ClientService {
    List<Client> findAll();
    void save(ClientRequest clientRequest);
}
