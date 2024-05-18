package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.common.Monitor;
import ru.otus.common.SequenceThread;
import ru.otus.enums.NameThread;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {
        logger.atInfo().setMessage("HomeWork 31").log();

        Queue<Integer> dataQueue = new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 9, 8, 7, 6, 5, 4, 3, 2));

        Monitor monitor = new Monitor(NameThread.ONE);

        Thread threadOne = new SequenceThread(NameThread.ONE, monitor, dataQueue);
        Thread threadTwo = new SequenceThread(NameThread.TWO, monitor, dataQueue);

        threadOne.start();
        threadTwo.start();

        threadOne.join();
        threadTwo.join();
    }
}