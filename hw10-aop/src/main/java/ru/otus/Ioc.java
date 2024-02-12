package ru.otus;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class Ioc {
    private Ioc() {}

    @SuppressWarnings("unchecked")
    public static <T> T create(T implementation, Class<T> clazz) {
        return (T) Proxy.newProxyInstance(
                clazz.getClassLoader(), new Class<?>[] {clazz}, iocInvocationHandler(implementation));
    }

    private static InvocationHandler iocInvocationHandler(Object implementation) {
        return new IocInvocationHandler(implementation);
    }
}
