package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.impl.CalculatorImpl;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final String ONE = "One";
    private static final String TWO = "Two";
    private static final String THREE = "Three";

    public static void main(String[] args) {
        logger.atInfo().setMessage("HomeWork 10").log();
        var calculatorInvocationHandler = new CalculatorInvocationHandler(new CalculatorImpl());
        var calculatorProxy = new CalculatorProxy(calculatorInvocationHandler);

        Calculator calculator = calculatorProxy.createCalculator();
        calculator.calculation(1);
        calculator.calculation(1, 2);
        calculator.calculation(1, 2, THREE);
        calculator.calculation(1, TWO, THREE);
        calculator.calculation(ONE, TWO, THREE);
    }
}
