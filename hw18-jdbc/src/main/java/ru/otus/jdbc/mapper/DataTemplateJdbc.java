package ru.otus.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
@SuppressWarnings({"java:S1068", "java:S3011"})
public class DataTemplateJdbc<T> implements DataTemplate<T> {
    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    var entityClassMetaData = entitySQLMetaData.getEntityClassMetaData();
                    List<Field> listField = entityClassMetaData.getAllFields();
                    Object[] arr = new Object[listField.size()];
                    for (int i = 0; i < arr.length; i++) {
                        arr[i] = (rs.getObject(listField.get(i).getName()));
                    }
                    Constructor<T> constructor = (Constructor<T>) entityClassMetaData.getConstructor();
                    return constructor.newInstance(arr);
                }
                return null;
            } catch (Exception e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        List<T> result = new ArrayList<>();
        dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), new ArrayList<>(), rs -> {
            try {
                while (rs.next()) {
                    var entityClassMetaData = entitySQLMetaData.getEntityClassMetaData();
                    List<Field> listField = entityClassMetaData.getAllFields();
                    Object[] arr = new Object[listField.size()];

                    for (int i = 0; i < arr.length; i++) {
                        arr[i] = (rs.getObject(listField.get(i).getName()));
                    }

                    Constructor<T> constructor = (Constructor<T>) entityClassMetaData.getConstructor();
                    result.add(constructor.newInstance(arr));
                }
                return null;
            } catch (Exception e) {
                throw new DataTemplateException(e);
            }
        });
        return result;
    }

    @Override
    public long insert(Connection connection, T object) {
        var entityClassMetaData = entitySQLMetaData.getEntityClassMetaData();
        List<Field> listObject = entityClassMetaData.getFieldsWithoutId();
        List<Object> list = new ArrayList<>();
        for (Field field : listObject) {
            field.setAccessible(true);
            try {
                list.add(field.get(object));
            } catch (IllegalAccessException e) {
                throw new DataTemplateException(e);
            }
        }
        try {
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), list);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T object) {
        var entityClassMetaData = entitySQLMetaData.getEntityClassMetaData();
        List<Field> listField = entityClassMetaData.getFieldsWithoutId();
        List<Object> listObject = new ArrayList<>();
        for (Field field : listField) {
            field.setAccessible(true);
            try {
                listObject.add(field.get(object));
            } catch (IllegalAccessException e) {
                throw new DataTemplateException(e);
            }
        }
        try {
            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), listObject);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }
}
