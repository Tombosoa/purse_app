package com.example.PurseApp.Repository;

import com.example.PurseApp.DataBaseConnection;
import com.example.PurseApp.Entity.Client;
import jakarta.el.PropertyNotFoundException;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ClientRepository implements CrudOperation<Client> {
    String userName = System.getenv("DB_USERNAME");
    String password = System.getenv("DB_PASSWORD");
    String databaseName = System.getenv("DB_NAME");
    DataBaseConnection dbConnection = new DataBaseConnection(userName, password, databaseName);
    Connection conn = dbConnection.getConnection();
    public Statement statement;

    @Override
    public List<Client> findAll() {
        List<Client> clientList = new ArrayList<>();
        try (Statement statement = conn.createStatement()) {
            String query = "SELECT * FROM client";
            try (ResultSet result = statement.executeQuery(query)) {
                while (result.next()) {
                    String id = result.getString("id");
                    String firstname = result.getString("firstname");
                    String lastname = result.getString("lastname");
                    Date birthdate = result.getDate("birthdate");
                    double monthlyPay = result.getDouble("monthlypay");
                    Client client = new Client(id,firstname, lastname, birthdate, monthlyPay);
                    clientList.add(client);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching users from the database", e);
        }
        return clientList;
    }

    @Override
    public List<Client> saveAll(List<Client> toSave) {
        return null;
    }

    @Override
    public Client save(Client toSave) {
        return null;
    }

    @Override
    public Client update(Client toUpdate) {
        return null;
    }

    @Override
    public Client delete(Client toDelete) {
        return null;
    }

    @Override
    public Client getOne(Client one) throws PropertyNotFoundException {
        return null;
    }
}
