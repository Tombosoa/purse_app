package com.example.PurseApp.Reflect;

import com.example.PurseApp.DataBaseConnection;
import jakarta.el.PropertyNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import java.util.Properties;
import java.util.UUID;

public class Service<T> implements AutoCrudOperation<T>{

    Class<T> entityClass;
    private final Connection conn;


    public Service(Class<T> entityClass) {
        Properties properties = new Properties();
        InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties");
        try {
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String userName = properties.getProperty("DB_USERNAME");
        String password = properties.getProperty("DB_PASSWORD");
        String databaseName = properties.getProperty("DB_NAME");

        this.entityClass = entityClass;
        this.conn = new DataBaseConnection(userName, password, databaseName).getConnection();
    }


    public static <T> Service<T> createService(Class<T> entityClass) {
        return new Service<>(entityClass);
    }

    public static <T> Service<T> createServiceWithoutType() {
        throw new RuntimeException("Service class must be parameterized with a type.");
    }

    public static String transformSelectQuery(Class<?> entityClass) {
        Field[] champs = entityClass.getDeclaredFields();

        StringBuilder columns = new StringBuilder();

        for (int i = 0; i < champs.length; i++) {
            columns.append(champs[i].getName());

            if (i < champs.length - 1) {
                columns.append(", ");
            }
        }

        String tableName = getSafeTableName(entityClass.getSimpleName().toLowerCase());

        return "SELECT " + columns + " FROM " + tableName;
    }
    private static String getSafeTableName(String tableName) {
        return "\"" + tableName + "\"";
    }
    @Override
    public List<T> findAll() {
        List<T> resultList = new ArrayList<>();

        try {
            String selectQuery = transformSelectQuery(entityClass);
            try (PreparedStatement preparedStatement = conn.prepareStatement(selectQuery);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                List<String> columnNames = getColumnNames();

                while (resultSet.next()) {
                    T entity = createEntityInstance();

                    for (String columnName : columnNames) {
                        try {
                            Field field = entityClass.getDeclaredField(columnName);
                            field.setAccessible(true);
                            Class<?> fieldType = field.getType();

                            if (fieldType.isEnum()) {
                                String enumValue = resultSet.getString(columnName);
                                if (enumValue != null) {
                                    Object[] enumConstants = fieldType.getEnumConstants();
                                    for (Object enumConstant : enumConstants) {
                                        if (enumValue.equalsIgnoreCase(enumConstant.toString())) {
                                            field.set(entity, enumConstant);
                                            break;
                                        }
                                    }
                                }
                            } else if (fieldType == String.class && columnName.equalsIgnoreCase("id")) {
                                UUID uuidValue = (UUID) resultSet.getObject(columnName);
                                String stringValue = uuidValue.toString();
                                field.set(entity, stringValue);
                            } else {
                                field.set(entity, resultSet.getObject(columnName));
                            }
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                    resultList.add(entity);
                }
            }
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return resultList;
    }



    private List<String> getColumnNames() {
        Field[] fields = entityClass.getDeclaredFields();
        List<String> columnNames = new ArrayList<>();
        for (Field field : fields) {
            columnNames.add(field.getName());
        }
        return columnNames;
    }

    private T createEntityInstance() throws IllegalAccessException, InstantiationException {
        return entityClass.newInstance();
    }
    @Override
    public List<T> saveAll(List<T> toSave) {
        return null;
    }


    public T save(T toSave) {
        String insertQuery = generateInsertQuery();

        try {
            try (PreparedStatement preparedStatement = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
                setPreparedStatementValues(preparedStatement, toSave);
                preparedStatement.executeUpdate();

                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    Field idField = entityClass.getDeclaredField("id");
                    idField.setAccessible(true);

                    if (idField.getType() == String.class) {
                        idField.set(toSave, generatedKeys.getObject(1).toString());
                    } else {
                        idField.set(toSave, generatedKeys.getObject(1));
                    }
                }
            }
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return toSave;
    }
    /*
     * This is a method for prepared statement, and also, catch if there's a column of type enum
     * */
    private void setPreparedStatementValues(PreparedStatement preparedStatement, T entity)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        List<String> columnNames = getColumnNamesWithoutId();

        for (int i = 0; i < columnNames.size(); i++) {
            String columnName = columnNames.get(i);
            Field field = entityClass.getDeclaredField(columnName);
            field.setAccessible(true);

            Object value = field.get(entity);


            if (field.getType().isEnum()) {
                preparedStatement.setObject(i + 1, value.toString(), Types.OTHER);
            } else {
                preparedStatement.setObject(i + 1, value);
            }
        }
    }

    /*
     * This method is for generating the correct insert query for the entity that extends this class
     * */
    private String generateInsertQuery() {
        String tableName = getSafeTableName(entityClass.getSimpleName().toLowerCase());
        List<String> columnNames = getColumnNamesWithoutId();

        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();

        for (int i = 0; i < columnNames.size(); i++) {
            columns.append(columnNames.get(i));
            values.append("?");

            if (i < columnNames.size() - 1) {
                columns.append(", ");
                values.append(", ");
            }
        }

        return "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + values + ")";
    }

    /*
     * this method get the column's name of the entity without the id using reflect
     * */
    private List<String> getColumnNamesWithoutId() {
        Field[] fields = entityClass.getDeclaredFields();
        List<String> columnNames = new ArrayList<>();
        for (Field field : fields) {
            if (!field.getName().equalsIgnoreCase("id")) {
                columnNames.add(field.getName());
            }
        }
        return columnNames;
    }

    @Override
    public T update(T toUpdate) {
        return null;
    }

    @Override
    public T delete(T toDelete) {
        return null;
    }

    @Override
    public T getOne(T one) throws PropertyNotFoundException {
        return null;
    }

}
