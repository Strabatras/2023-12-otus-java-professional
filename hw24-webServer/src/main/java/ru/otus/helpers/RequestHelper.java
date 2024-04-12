package ru.otus.helpers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;

import java.util.Arrays;
import java.util.List;

import static java.util.Objects.isNull;

@UtilityClass
public final class RequestHelper {

    private static final int MAX_INACTIVE_INTERVAL = 30;

    public static Client requestToClient(HttpServletRequest request){
        String name = request.getParameter("name");
        Address address = addressParameterToAddress(request);
        List<Phone> phones = addressParameterToPhoneList(request);
        return new Client(null, name, address, phones);
    }

    public static Address addressParameterToAddress(HttpServletRequest request){
        String address = request.getParameter("address");
        if (isNull(address)){
            return null;
        }
        return new Address(null, address);
    }
    public static List<Phone> addressParameterToPhoneList(HttpServletRequest request){
        String phones = request.getParameter("phones");
        if (isNull(phones)){
            return null;
        }
        return Arrays.stream(phones.split("\\n"))
                .map(phone -> new Phone(null, phone))
                .toList();
    }
}
