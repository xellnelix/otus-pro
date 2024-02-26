package ru.otus.jdbc.mapper;

/** Создает SQL - запросы */
@SuppressWarnings({"java:S1452"})
public interface EntitySQLMetaData {
    String getSelectAllSql();

    String getSelectByIdSql();

    String getInsertSql();

    String getUpdateSql();

    EntityClassMetaData<?> getEntityClassMetaData();
}
