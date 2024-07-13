package ru.otus.service.impl;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.Client;
import ru.otus.service.NumberRequest;
import ru.otus.service.NumberResponse;
import ru.otus.service.NumberService;
import ru.otus.service.NumberServiceGrpc;

import static ru.otus.common.Util.secondsToSleep;


public class NumberServiceImpl extends NumberServiceGrpc.NumberServiceImplBase implements NumberService {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    @Override
    public void runSequence(NumberRequest request, StreamObserver<NumberResponse> responseObserver) {
        logger.atInfo().setMessage(
                "Run sequence from %d to %d".formatted(request.getFirstValue(), request.getLastValue())
        ).log();

        for (long i = request.getFirstValue(); i < request.getLastValue(); i++) {
            var response = NumberResponse.newBuilder()
                    .setNumber(i)
                    .build();
            responseObserver.onNext(response);
            secondsToSleep(2, logger);
        }
        responseObserver.onCompleted();
    }
}
