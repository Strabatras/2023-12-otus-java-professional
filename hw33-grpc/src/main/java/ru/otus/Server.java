package ru.otus;

import io.grpc.BindableService;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.service.NumberService;
import ru.otus.service.impl.NumberServiceImpl;

import java.io.IOException;

import static ru.otus.common.Properties.SERVER_PORT;

public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {
        NumberService numberService = new NumberServiceImpl();
        io.grpc.Server server = ServerBuilder
                .forPort(SERVER_PORT)
                .addService((BindableService) numberService)
                .build();
        try {
            server.start();
            logger.atInfo().setMessage("Server as awaiting for connections").log();
            server.awaitTermination();
        } catch (IOException | InterruptedException e) {
            logger.atError().setCause(e).log();
        }
    }
}
