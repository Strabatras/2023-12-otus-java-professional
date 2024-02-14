package ru.otus.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.Calculator;
import ru.otus.Log;

@SuppressWarnings("java:S1192")
public class CalculatorImpl implements Calculator {
    private static final Logger logger = LoggerFactory.getLogger(CalculatorImpl.class);

    @Log
    @Override
    public void calculation(int param1) {
        logger.atInfo().setMessage("run calculation(" + param1 + ")").log();
    }

    @Log
    @Override
    public void calculation(int param1, int param2) {
        logger.atInfo()
                .setMessage("run calculation(" + param1 + ", " + param2 + ")")
                .log();
    }

    @Log
    @Override
    public void calculation(int param1, int param2, String param3) {
        logger.atInfo()
                .setMessage("run calculation(" + param1 + ", " + param2 + ", " + param3 + ")")
                .log();
    }

    @Override
    public void calculation(int param1, String param2, String param3) {
        logger.atInfo()
                .setMessage("run calculation(" + param1 + ", " + param2 + ", " + param3 + ")")
                .log();
    }

    @Log
    @Override
    public void calculation(String param1, String param2, String param3) {
        logger.atInfo()
                .setMessage("run calculation(" + param1 + ", " + param2 + ", " + param3 + ")")
                .log();
    }
}
