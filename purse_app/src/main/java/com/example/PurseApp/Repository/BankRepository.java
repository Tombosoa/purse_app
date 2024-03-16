package com.example.PurseApp.Repository;

import com.example.PurseApp.DataBaseConnection;
import com.example.PurseApp.Entity.Bank;
import jakarta.el.PropertyNotFoundException;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class BankRepository implements CrudOperation<Bank>{
    String userName = System.getenv("DB_USERNAME");
    String password = System.getenv("DB_PASSWORD");
    String databaseName = System.getenv("DB_NAME");
    DataBaseConnection dbConnection = new DataBaseConnection(userName, password, databaseName);
    Connection conn = dbConnection.getConnection();
    public Statement statement;

    @Override
    public List<Bank> findAll() {
        List<Bank> bankList = new ArrayList<>();
        try (Statement statement = conn.createStatement()) {
            String query = "SELECT * FROM bank";
            try (ResultSet resultSet = statement.executeQuery(query)){
                while(resultSet.next()){
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");

                    Bank bank = new Bank(id, name);
                    bankList.add(bank);
                }
            }
        }catch (SQLException e){
            throw new RuntimeException("Error fetching client from the database", e);
        }
        return bankList;
    }

    @Override
    public List<Bank> saveAll(List<Bank> toSave) {
        return null;
    }

    @Override
    public Bank save(Bank toSave) {
        try {
            String query = "INSERT INTO bank (name) VALUES (?)";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, toSave.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return toSave;
    }

    @Override
    public Bank update(Bank toUpdate) {
        return null;
    }

    @Override
    public Bank delete(Bank toDelete) {
        return null;
    }

    @Override
    public Bank getOne(Bank one) throws PropertyNotFoundException {
        return null;
    }
}
