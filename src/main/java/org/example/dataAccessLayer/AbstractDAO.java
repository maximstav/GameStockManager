package org.example.dataAccessLayer;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.example.connection.ConnectionFactory;

public class AbstractDAO<T> {
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

    private final Class<T> type;

    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    }

    private String createSelectQuery(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append(" FROM ");
        sb.append(getTableName());
        sb.append(" WHERE " + field + " =?");
        return sb.toString();
    }

    public List<T> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ");
        sb.append(getTableName());

        String query = sb.toString();

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            List<T> results = createObjects(resultSet);
            if (results == null || results.isEmpty()) {
                LOGGER.log(Level.INFO, "No " + type.getSimpleName() + " records found");
                return new ArrayList<>();
            }
            return results;
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findAll " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        return new ArrayList<>(); // Return empty list instead of null
    }

    public T findById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("id");
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            return createObjects(resultSet).get(0);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<>();

        try {
            Constructor<T> ctor = (Constructor<T>) Arrays.stream(type.getDeclaredConstructors())
                    .filter(c -> c.getParameterCount() > 0)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No suitable constructor found"));

            while (resultSet.next()) {
                // For each constructor parameter, get the corresponding value from ResultSet
                Class<?>[] paramTypes = ctor.getParameterTypes();
                Object[] params = new Object[paramTypes.length];

                for (int i = 0; i < paramTypes.length; i++) {
                    // Assuming constructor parameters order matches DB columns order
                    params[i] = resultSet.getObject(i + 1);
                }

                T instance = ctor.newInstance(params);
                list.add(instance);
            }
        } catch (SQLException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return list;
    }


    // createObjects method that uses an empty constructor and then sets each field using setters
    // however records / immutable classes (like Bill) don't have setters and empty constructors
/*
    private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<T>();
        Constructor[] ctors = type.getDeclaredConstructors();
        Constructor ctor = null;
        for (int i = 0; i < ctors.length; i++) {
            ctor = ctors[i];
            if (ctor.getGenericParameterTypes().length == 0)
                break;
        }
        try {
            while (resultSet.next()) {
                ctor.setAccessible(true);
                T instance = (T)ctor.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    String fieldName = field.getName();
                    Object value = resultSet.getObject(fieldName);
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return list;
    }
*/
    public T insert(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionFactory.getConnection();

            // Extract field names and values from the object
            Field[] fields = type.getDeclaredFields();
            List<String> fieldNames = new ArrayList<>();
            List<Object> values = new ArrayList<>();

            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();

                // Skip 'id' field assuming it's auto-incremented
                if (!fieldName.equals("id")) {
                    fieldNames.add(fieldName);
                    values.add(field.get(t));
                }
            }

            // Construct the SQL INSERT query
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("INSERT INTO ");
            queryBuilder.append(getTableName());
            queryBuilder.append(" (");
            queryBuilder.append(String.join(", ", fieldNames));
            queryBuilder.append(") VALUES (");

            // Create the parameter placeholders
            for (int i = 0; i < fieldNames.size(); i++) {
                if (i > 0) {
                    queryBuilder.append(", ");
                }
                queryBuilder.append("?");
            }
            queryBuilder.append(")");

            String query = queryBuilder.toString();
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            // Set parameter values
            for (int i = 0; i < values.size(); i++) {
                statement.setObject(i + 1, values.get(i));
            }

            // Execute the insert statement
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating record failed, no rows affected.");
            }

            // Get the generated ID
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                int generatedId = resultSet.getInt(1);

                // Try to set the ID to the object
                try {
                    Field idField = type.getDeclaredField("id");
                    idField.setAccessible(true);
                    idField.set(t, generatedId);
                } catch (NoSuchFieldException e) {
                    LOGGER.log(Level.WARNING, "No id field found in " + type.getName());
                }
            }

            return t;
        } catch (SQLException | IllegalAccessException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        return null;
    }

    public T update(T t) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = ConnectionFactory.getConnection();

            // Extract field names and values from the object
            Field[] fields = type.getDeclaredFields();
            List<String> fieldNames = new ArrayList<>();
            List<Object> values = new ArrayList<>();
            Integer id = null;

            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object value = field.get(t);

                if (fieldName.equals("id")) {
                    id = (Integer) value;
                } else {
                    fieldNames.add(fieldName);
                    values.add(value);
                }
            }

            if (id == null) {
                throw new SQLException("Cannot update object without id");
            }

            // Construct the SQL UPDATE query
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("UPDATE ");
            queryBuilder.append(getTableName());
            queryBuilder.append(" SET ");

            // Add field assignments
            for (int i = 0; i < fieldNames.size(); i++) {
                if (i > 0) {
                    queryBuilder.append(", ");
                }
                queryBuilder.append(fieldNames.get(i)).append(" = ?");
            }

            queryBuilder.append(" WHERE id = ?");

            String query = queryBuilder.toString();
            statement = connection.prepareStatement(query);

            // Set parameter values
            for (int i = 0; i < values.size(); i++) {
                statement.setObject(i + 1, values.get(i));
            }
            // Set the id parameter
            statement.setInt(values.size() + 1, id);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                LOGGER.log(Level.WARNING, "No rows affected when updating " + type.getName() + " with id " + id);
                return null;
            }

            return t;
        } catch (SQLException | IllegalAccessException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:update " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        return null;
    }

    protected String getTableName() {
        return type.getSimpleName(); // default behavior
    }
}

