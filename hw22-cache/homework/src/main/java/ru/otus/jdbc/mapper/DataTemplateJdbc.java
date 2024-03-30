package ru.otus.jdbc.mapper;

import static java.util.Collections.emptyList;
import static java.util.List.of;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

/** Сохратяет объект в базу, читает объект из базы */
@SuppressWarnings({"java:S1068", "java:S3011"})
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(
            DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), of(id), rs -> {
            try {
                return (rs.next()) ? rsToEntity(rs) : null;
            } catch (Exception e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor
                .executeSelect(connection, entitySQLMetaData.getSelectAllSql(), emptyList(), rs -> {
                    try {
                        List<T> entityList = new ArrayList<>();
                        while (rs.next()) {
                            entityList.add(rsToEntity(rs));
                        }
                        return entityList;
                    } catch (Exception e) {
                        throw new DataTemplateException(e);
                    }
                })
                .orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T entity) {
        try {
            return dbExecutor.executeStatement(
                    connection,
                    entitySQLMetaData.getInsertSql(),
                    prepareEntityFieldValues(entity, entityClassMetaData.getFieldsWithoutId()));
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T entity) {
        try {
            dbExecutor.executeStatement(
                    connection,
                    entitySQLMetaData.getUpdateSql(),
                    prepareEntityFieldValuesWithId(entity, entityClassMetaData.getFieldsWithoutId()));
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    private T rsToEntity(ResultSet rs)
            throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Object[] args = IntStream.range(0, entityClassMetaData.getConstructor().getParameterCount())
                .filter(i -> i <= entityClassMetaData.getConstructor().getParameterCount())
                .mapToObj(i -> {
                    try {
                        return rs.getObject(
                                entityClassMetaData.getAllFields().get(i).getName());
                    } catch (SQLException e) {
                        throw new DataTemplateException(e);
                    }
                })
                .toArray();
        return entityClassMetaData.getConstructor().newInstance(args);
    }

    private List<Object> prepareEntityFieldValues(T entity, List<Field> fieldList) throws IllegalAccessException {
        List<Object> fieldValues = new ArrayList<>();
        for (Field field : fieldList) {
            field.setAccessible(true);
            fieldValues.add(field.get(entity));
        }
        return fieldValues;
    }

    private List<Object> prepareEntityFieldValuesWithId(T entity, List<Field> fieldList) throws IllegalAccessException {
        List<Object> fieldValues = prepareEntityFieldValues(entity, fieldList);
        Field field = entityClassMetaData.getIdField();
        field.setAccessible(true);
        fieldValues.add(field.get(entity));
        return fieldValues;
    }
}
