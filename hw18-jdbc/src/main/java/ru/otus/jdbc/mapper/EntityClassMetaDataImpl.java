package ru.otus.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.crm.model.Id;

@SuppressWarnings({"java:S3740"})
public class EntityClassMetaDataImpl<T> implements EntityClassMetaData {
    private final Class<T> clazz;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String getName() {
        return clazz.getSimpleName();
    }

    @Override
    public Constructor getConstructor() {
        List<Field> listField = getAllFields();
        Class[] classes = new Class[listField.size()];
        for (int i = 0; i < classes.length; i++) {
            classes[i] = listField.get(i).getType();
        }
        Constructor<T> constructor;
        try {
            constructor = clazz.getConstructor(classes);
        } catch (NoSuchMethodException e) {
            throw new DataTemplateException(e);
        }
        return constructor;
    }

    @Override
    public Field getIdField() {
        for (Field field : getAllFields()) {
            if (field.getDeclaredAnnotation(Id.class) != null) {
                return field;
            }
        }
        return null;
    }

    @Override
    public List<Field> getAllFields() {
        return List.of(clazz.getDeclaredFields());
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return getAllFields().stream()
                .filter(field -> field.getDeclaredAnnotation(Id.class) == null)
                .toList();
    }
}
