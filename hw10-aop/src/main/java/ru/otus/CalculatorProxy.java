package ru.otus;

import java.lang.reflect.Proxy;

public class CalculatorProxy {
    private final CalculatorInvocationHandler calculatorInvocationHandler;

    public CalculatorProxy(CalculatorInvocationHandler calculatorInvocationHandler) {
        this.calculatorInvocationHandler = calculatorInvocationHandler;
    }

    public Calculator createCalculator() {
        return (Calculator) Proxy.newProxyInstance(
                Calculator.class.getClassLoader(), new Class<?>[] {Calculator.class}, calculatorInvocationHandler);
    }
}
