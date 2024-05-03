package ru.otus.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.model.Client;
import ru.otus.repository.ClientRepository;
import ru.otus.request.ClientRequest;
import ru.otus.service.ClientService;

import java.util.List;

import static ru.otus.helpers.RequestHelper.clientRequestToAddress;
import static ru.otus.helpers.RequestHelper.clientRequestToId;
import static ru.otus.helpers.RequestHelper.clientRequestToName;
import static ru.otus.helpers.RequestHelper.clientRequestToPhones;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Transactional
    @Override
    public void save(ClientRequest clientRequest){
        Client client = new Client(
                clientRequestToId(clientRequest),
                clientRequestToName(clientRequest),
                clientRequestToAddress(clientRequest),
                clientRequestToPhones(clientRequest)
        );
        clientRepository.save(client);
    }
}
