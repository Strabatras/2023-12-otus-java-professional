package ru.otus.jdbc.mapper.impl;

import static java.lang.String.format;
import static java.lang.String.join;
import static java.util.Arrays.fill;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.EntitySQLMetaData;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {

    private static final String JOIN_FIELD_DELIMITER = ",";
    private final EntityClassMetaData<T> entityClassMetaData;
    private final String[] fieldNames;
    private final String[] fieldNamesWithoutId;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
        this.fieldNames = fieldListToNameList(entityClassMetaData.getAllFields());
        this.fieldNamesWithoutId = fieldListToNameList(entityClassMetaData.getFieldsWithoutId());
    }

    @Override
    public String getSelectAllSql() {
        return format("SELECT %s FROM %s",
                join(JOIN_FIELD_DELIMITER, fieldNames),
                entityClassMetaData.getName()
        );
    }

    @Override
    public String getSelectByIdSql() {
        return format(
                "SELECT %s FROM %s WHERE %s = ?",
                join(JOIN_FIELD_DELIMITER, fieldNames),
                entityClassMetaData.getName(),
                entityClassMetaData.getIdField().getName());
    }

    @Override
    public String getInsertSql() {
        String[] values = new String[fieldNamesWithoutId.length];
        fill(values, "?");
        return format(
                "INSERT INTO %s ( %s ) VALUES ( %s )",
                entityClassMetaData.getName(),
                join(JOIN_FIELD_DELIMITER, fieldNamesWithoutId),
                join(JOIN_FIELD_DELIMITER, values));
    }

    @Override
    public String getUpdateSql() {
        return format(
                "UPDATE %s SET %s WHERE %s = ?",
                entityClassMetaData.getName(),
                join(JOIN_FIELD_DELIMITER, fieldNamesToNamePairs(fieldNamesWithoutId)),
                entityClassMetaData.getIdField().getName());
    }

    private String[] fieldListToNameList(List<Field> fieldList) {
        return fieldList.stream().map(Field::getName).toArray(String[]::new);
    }

    private String[] fieldNamesToNamePairs(String[] fieldNames) {
        return Arrays.stream(fieldNames).map(name -> name + " = ?").toArray(String[]::new);
    }
}
