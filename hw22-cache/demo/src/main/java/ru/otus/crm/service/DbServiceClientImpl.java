package ru.otus.crm.service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionRunner;
import ru.otus.crm.model.Client;

import static java.lang.String.valueOf;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<Client> dataTemplate;
    private final TransactionRunner transactionRunner;
    private final HwCache<String, Client> cache;

    public DbServiceClientImpl(TransactionRunner transactionRunner, DataTemplate<Client> dataTemplate, HwCache<String, Client> cache) {
        this.transactionRunner = transactionRunner;
        this.dataTemplate = dataTemplate;
        this.cache = cache;
    }

    @Override
    public Client saveClient(Client client) {
        Client savedClient = transactionRunner.doInTransaction(connection -> {
            if (client.getId() == null) {
                var clientId = dataTemplate.insert(connection, client);
                var createdClient = new Client(clientId, client.getName());
                //log.info("created client: {}", createdClient);
                return createdClient;
            }
            dataTemplate.update(connection, client);
            //log.info("updated client: {}", client);
            return client;
        });
        putToCache(savedClient);
        return savedClient;
    }

    @Override
    public Optional<Client> getClient(long id) {
        Client cachedClient = cache.get(valueOf(id));
        if (cachedClient != null) {
            return Optional.of(cachedClient);
        }
        Optional<Client> client = transactionRunner.doInTransaction(connection -> {
            var clientOptional = dataTemplate.findById(connection, id);
            //log.info("client: {}", clientOptional);
            return clientOptional;
        });
        if (client.isPresent()){
            putToCache(client.get());
        }
        return client;
    }

    @Override
    public List<Client> findAll() {
        return transactionRunner.doInTransaction(connection -> {
            var clientList = dataTemplate.findAll(connection);
            //log.info("clientList:{}", clientList);
            return clientList;
        });
    }

    private void putToCache(Client client){
        if (client != null) {
            cache.put(client.getId().toString(), client);
        }
    }
}
