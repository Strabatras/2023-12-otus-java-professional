package ru.otus.helpers;

import lombok.experimental.UtilityClass;
import ru.otus.model.Address;
import ru.otus.model.Phone;
import ru.otus.request.ClientRequest;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public final class RequestHelper {

    public static Long clientRequestToId(ClientRequest clientRequest){
        return clientRequest.id();
    }

    public static String clientRequestToName(ClientRequest clientRequest){
        if (clientRequest.name() == null || clientRequest.name().trim().isEmpty()){
            return null;
        }
        return clientRequest.name().trim();
    }

    public static Address clientRequestToAddress(ClientRequest clientRequest){
        if (clientRequest.address() == null || clientRequest.address().trim().isEmpty()){
            return null;
        }
        return new Address(null, clientRequest.address().trim(), clientRequest.id());
    }

    public static Set<Phone> clientRequestToPhones(ClientRequest clientRequest){
        if (clientRequest.phones() == null || clientRequest.phones().trim().isEmpty()){
            return null;
        }
        return Arrays.stream(clientRequest.phones().trim().split("\\n"))
                .map(phone -> new Phone(null, phone, clientRequest.id()))
                .collect(Collectors.toSet());
    }
}
