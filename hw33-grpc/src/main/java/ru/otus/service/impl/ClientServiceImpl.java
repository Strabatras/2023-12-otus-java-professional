package ru.otus.service.impl;

import io.grpc.ManagedChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.common.NumberStreamObserver;
import ru.otus.service.ClientService;
import ru.otus.service.NumberRequest;
import ru.otus.service.NumberServiceGrpc;

import static ru.otus.common.Properties.CLIENT_FIRST_VALUE;
import static ru.otus.common.Properties.CLIENT_LAST_VALUE;
import static ru.otus.common.Properties.SERVER_FIRST_VALUE;
import static ru.otus.common.Properties.SERVER_LAST_VALUE;
import static ru.otus.common.Util.secondsToSleep;

public class ClientServiceImpl implements ClientService {

    private static final Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);
    private final NumberServiceGrpc.NumberServiceStub numberService;
    private long currentValue = 0;

    public ClientServiceImpl(ManagedChannel channel) {
        this.numberService = NumberServiceGrpc.newStub(channel);
    }

    public void run() {
        var request = getNumberRequest();
        var observer = new NumberStreamObserver();
        numberService.runSequence(request, observer);

        for (long i = CLIENT_FIRST_VALUE; i < CLIENT_LAST_VALUE; i++) {
            long numberFromServer = observer.monitor.getValueAndReset();
            logger.atInfo().setMessage(
                    "number from server %d".formatted(numberFromServer)
            ).log();
            currentValue = currentValue + numberFromServer + 1;
            logger.atInfo().setMessage(
                    "current value %d".formatted(currentValue)
            ).log();
            secondsToSleep(1, logger);
        }
    }

    private NumberRequest getNumberRequest() {
        return NumberRequest.newBuilder()
                .setFirstValue(SERVER_FIRST_VALUE)
                .setLastValue(SERVER_LAST_VALUE)
                .build();
    }

}
