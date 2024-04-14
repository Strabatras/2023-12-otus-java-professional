package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Comparator.comparing;
import static java.util.Optional.ofNullable;

@SuppressWarnings("squid:S1068")
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass)  {
        checkConfigClass(configClass);
        final List<Method> methodList = configClassAnnotatedMethodList(configClass);
        try {
            final Object configClassInstance = configClass.getDeclaredConstructor().newInstance();
            for (Method method : methodList){
                String componentName = getComponentName(method);
                appComponentsByName.computeIfPresent(componentName, (key, val) -> {
                    throw new RuntimeException(
                            "'%s'. Trying of adding component with existing name".formatted(key)
                    );
                });

                Object[] componentArguments = getComponentArguments(method);

                var component = method.invoke(configClassInstance, componentArguments);
                appComponents.add(component);
                appComponentsByName.put(componentName, component);
            }
        } catch ( NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e){
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(
                    "Given class is not config %s".formatted(configClass.getName())
            );
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        var components = appComponents.stream()
                .filter(component -> componentClass.isAssignableFrom(component.getClass()))
                .toList();

        if (components.size() == 0 ){
            throw new RuntimeException("Attempt of getting missing component");
        }

        if (components.size() > 1 ){
            throw new RuntimeException("Attempt of getting duplicative component");
        }
        return (C) components.get(0);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return ofNullable((C) appComponentsByName.get(componentName))
                .orElseThrow(() -> new RuntimeException(
                        "'%s'. Attempt of getting missing component".formatted(componentName)
                ));
    }

    private List<Method> configClassAnnotatedMethodList(Class<?> configClass){
        return Arrays.stream(configClass.getMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(comparing(method -> method.getAnnotation(AppComponent.class).order()))
                .toList();
    }

    private String getComponentName(Method method) {
        return method.getAnnotation(AppComponent.class).name();
    }

    private Object[] getComponentArguments(Method method) {
        return Arrays.stream(method.getParameters())
                .map(parameter -> getAppComponent(parameter.getType()))
                .toArray();
    }
}
