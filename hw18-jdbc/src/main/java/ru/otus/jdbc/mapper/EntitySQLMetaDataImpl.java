package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
    private final EntityClassMetaData<?> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    public EntityClassMetaData<?> getEntityClassMetaData() {
        return entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return String.format("select * from %s", entityClassMetaData.getName());
    }

    @Override
    public String getSelectByIdSql() {
        return String.format(
                "select * from %s where %s  = ?",
                entityClassMetaData.getName(), entityClassMetaData.getIdField().getName());
    }

    @Override
    public String getInsertSql() {
        String fields = entityClassMetaData.getFieldsWithoutId().stream()
                .map(Field::getName)
                .collect(Collectors.joining(","));
        String values = Arrays.stream(fields.split(",")).map(s -> "?").collect(Collectors.joining(","));
        return String.format("insert into %s (%s) values (%s)", entityClassMetaData.getName(), fields, values);
    }

    @Override
    public String getUpdateSql() {
        String fields = entityClassMetaData.getFieldsWithoutId().stream()
                .map(field -> field + " = ?")
                .collect(Collectors.joining(","));
        return String.format("update %s set %s where id = ?", entityClassMetaData.getName(), fields);
    }
}
