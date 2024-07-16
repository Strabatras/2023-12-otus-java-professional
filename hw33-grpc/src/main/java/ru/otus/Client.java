package ru.otus;

import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.service.impl.ClientServiceImpl;

import static ru.otus.common.Properties.SERVER_HOST;
import static ru.otus.common.Properties.SERVER_PORT;

public class Client {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) {

        var channel = ManagedChannelBuilder
                .forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        logger.atInfo().setMessage("Run number client").log();

        var numberClient = new ClientServiceImpl(channel);
        numberClient.run();

        logger.atInfo().setMessage("Finish number client").log();
        channel.shutdown();
    }
}