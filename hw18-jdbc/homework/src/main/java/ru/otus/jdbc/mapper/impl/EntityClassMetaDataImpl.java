package ru.otus.jdbc.mapper.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import ru.otus.annotations.Id;
import ru.otus.jdbc.mapper.EntityClassMetaData;

@SuppressWarnings("unchecked")
public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final String name;
    private final Constructor<T> constructor;
    private final Field idField;
    private final List<Field> allFields;
    private final List<Field> fieldsWithoutId;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.name = clazz.getSimpleName();
        this.constructor = prepareConstructor(clazz);
        this.idField = prepareIdField(clazz).orElse(null);
        this.allFields = prepareAllFields(clazz);
        this.fieldsWithoutId = prepareFieldsWithoutId(clazz);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId;
    }

    private Constructor<T> prepareConstructor(Class<T> clazz) {
        return (Constructor<T>) Arrays.stream(clazz.getConstructors())
                .max(Comparator.comparing(Constructor::getParameterCount))
                .orElseThrow(() -> new RuntimeException("Can't get constructor of entity"));
    }

    private Optional<Field> prepareIdField(Class<T> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst();
    }

    private List<Field> prepareAllFields(Class<T> clazz) {
        return List.of(clazz.getDeclaredFields());
    }

    private List<Field> prepareFieldsWithoutId(Class<T> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> !field.isAnnotationPresent(Id.class))
                .toList();
    }
}
