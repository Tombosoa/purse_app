package com.example.PurseApp.Repository;

import com.example.PurseApp.DataBaseConnection;
import com.example.PurseApp.Entity.Category;
import jakarta.el.PropertyNotFoundException;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Component
public class CategoryRepository implements CrudOperation<Category>{
    String userName = System.getenv("DB_USERNAME");
    String password = System.getenv("DB_PASSWORD");
    String databaseName = System.getenv("DB_NAME");
    DataBaseConnection dbConnection = new DataBaseConnection(userName, password, databaseName);
    Connection conn = dbConnection.getConnection();
    public Statement statement;
    @Override
    public List<Category> findAll() {
        return null;
    }

    @Override
    public List<Category> saveAll(List<Category> toSave) {
        return null;
    }

    @Override
    public Category save(Category toSave) {
        return null;
    }

    @Override
    public Category update(Category toUpdate) {
        return null;
    }

    @Override
    public Category delete(Category toDelete) {
        return null;
    }

    @Override
    public Category getOne(Category one) throws PropertyNotFoundException {
        return null;
    }

    public Category getByTypeAndName(String type, String name){
        try{
            String query = "SELECT id, type, name, description FROM category WHERE name = ? and type = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setObject(1, name, Types.OTHER);
            preparedStatement.setObject(2, type, Types.OTHER);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                Category category = new Category();
                category.setId(resultSet.getInt("id"));
                category.setName(resultSet.getString("name"));
                category.setType(resultSet.getString("type"));
                category.setDescription(resultSet.getString("description"));

                return category;
            }else {
                throw new PropertyNotFoundException("category not found");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Map<String, Object>> sumAmountsByCategory(LocalDate startDate, LocalDate endDate, UUID accountId) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<>();
        String sql = "SELECT * FROM SumAmountsByCategory(?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));
            stmt.setObject(3, accountId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> result = new HashMap<>();
                    result.put("category_id", rs.getInt("category_id"));
                    result.put("category_name", rs.getString("category_name"));
                    result.put("total_amount", rs.getDouble("total_amount"));
                    results.add(result);
                }
            }
        }
        return results;
    }

}
