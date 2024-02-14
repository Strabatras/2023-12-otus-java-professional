package ru.otus;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IocInvocationHandler implements InvocationHandler {
    private final Object obj;
    private final Map<String, List<String>> annotatedMethodParam;
    private final Logger logger;

    public IocInvocationHandler(Object obj) {
        this.obj = obj;
        this.annotatedMethodParam = fillAnnotatedMethodParamList(obj);
        this.logger = LoggerFactory.getLogger(IocInvocationHandler.class);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (isAnnotatedMethod(method)) {
            toLogger(method, args);
        }
        return method.invoke(obj, args);
    }

    private Map<String, List<String>> fillAnnotatedMethodParamList(Object obj) {
        Map<String, List<String>> annotatedMethod = new HashMap<>();
        for (Method method : obj.getClass().getMethods()) {

            if (method.isAnnotationPresent(Log.class)) {
                String methodName = method.getName();
                List<String> methodParams = annotatedMethod.computeIfAbsent(methodName, key -> new ArrayList<>());
                String genericParameterTypesToString = genericParameterTypesToString(method);
                methodParams.add(genericParameterTypesToString);
                annotatedMethod.put(methodName, methodParams);
            }
        }
        return annotatedMethod;
    }

    private String genericParameterTypesToString(Method method) {
        String[] types = Arrays.stream(method.getGenericParameterTypes())
                .map(Object::toString)
                .toArray(String[]::new);
        return String.join(", ", types);
    }

    private String methodArgumentsToString(Object[] args) {
        String[] arguments = Arrays.stream(args).map(Object::toString).toArray(String[]::new);
        return String.join(", ", arguments);
    }

    private boolean isAnnotatedMethod(Method method) {
        final List<String> methodParams =
                annotatedMethodParam.computeIfAbsent(method.getName(), key -> new ArrayList<>());
        final String typesToString = genericParameterTypesToString(method);
        return methodParams.stream().anyMatch(types -> types.equals(typesToString));
    }

    private void toLogger(Method method, Object[] args) {
        String argumentsToString = methodArgumentsToString(args);
        logger.atInfo()
                .setMessage("Log : executed method: " + method.getName() + ", param(s): " + argumentsToString)
                .log();
    }
}
