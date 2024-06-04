package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.common.Monitor;
import ru.otus.common.SequenceThread;
import ru.otus.enums.NameThread;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {
        logger.atInfo().setMessage("HomeWork 31").log();

        final Monitor monitor = new Monitor(NameThread.ONE);

        Thread threadOne = new SequenceThread(NameThread.ONE, monitor);
        Thread threadTwo = new SequenceThread(NameThread.TWO, monitor);

        threadOne.start();
        threadTwo.start();

        threadOne.join();
        threadTwo.join();
    }
}