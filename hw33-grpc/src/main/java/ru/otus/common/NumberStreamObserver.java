package ru.otus.common;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.service.NumberResponse;

public class NumberStreamObserver implements StreamObserver<NumberResponse> {

    private static final Logger logger = LoggerFactory.getLogger(NumberStreamObserver.class);

    private long responseValue = 0;

    @Override
    public void onNext(NumberResponse numberResponse) {
        setResponseValue(numberResponse.getNumber());
        logger.atInfo().setMessage(
                "new value: %d".formatted(numberResponse.getNumber())
        ).log();
    }

    @Override
    public void onError(Throwable throwable) {
        logger.atError().setCause(throwable).log();
    }

    @Override
    public void onCompleted() {
        logger.atInfo().setMessage("onCompleted").log();
    }

    public synchronized void setResponseValue(long responseValue) {
        this.responseValue = responseValue;
    }

    public synchronized long getResponseValueAndReset() {
        final long value = this.responseValue;
        this.responseValue = 0;
        return value;
    }
}
