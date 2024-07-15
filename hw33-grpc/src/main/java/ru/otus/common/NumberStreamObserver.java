package ru.otus.common;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.service.NumberResponse;

public class NumberStreamObserver implements StreamObserver<NumberResponse> {

    private static final Logger logger = LoggerFactory.getLogger(NumberStreamObserver.class);

    public final Monitor monitor = new Monitor();

    @Override
    public void onNext(NumberResponse numberResponse) {
        monitor.setValue(numberResponse.getNumber());
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

    public class Monitor {

        private long value = 0;

        public synchronized void setValue(long value) {
            this.value = value;
        }

        public synchronized long getValueAndReset() {
            final long resultValue = this.value;
            this.value = 0;
            return resultValue;
        }
    }
}
