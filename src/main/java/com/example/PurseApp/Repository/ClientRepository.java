package com.example.PurseApp.Repository;

import com.example.PurseApp.DataBaseConnection;
import com.example.PurseApp.Entity.Client;
import jakarta.el.PropertyNotFoundException;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.UUID;

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
            throw new RuntimeException("Error fetching client from the database", e);
        }
        return clientList;
    }

    @Override
    public List<Client> saveAll(List<Client> toSave) {
        List<Client> savedClients = new ArrayList<>();
        try {
            for (Client client : toSave) {
                String query = "INSERT INTO client (firstname, lastname, birthdate, monthlypay) VALUES (?, ?, ?, ?)";
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setString(1, client.getFirstname());
                preparedStatement.setString(2, client.getLastname());
                preparedStatement.setObject(3, client.getBirthdate());
                preparedStatement.setDouble(4, client.getMonthlyPay());

                preparedStatement.executeUpdate();
                savedClients.add(client);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return savedClients;
    }

    @Override
    public Client save(Client toSave) {
        String idClient;
        try {
            String query = "INSERT INTO client (firstname, lastname, birthdate, monthlypay) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, toSave.getFirstname());
            preparedStatement.setString(2, toSave.getLastname());
            preparedStatement.setDate(3, toSave.getBirthdate());
            preparedStatement.setDouble(4, toSave.getMonthlyPay());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()){
                idClient = generatedKeys.getString(1);
                toSave.setId(idClient);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return toSave;
    }

    @Override
    public Client update(Client toUpdate) {
        try {
            String updateQuery = "UPDATE client SET firstname=?, lastname = ?, birthdate = ?, monthlypay=?";
            PreparedStatement updateStatement = conn.prepareStatement(updateQuery);
            updateStatement.setString(1, toUpdate.getFirstname());
            updateStatement.setString(2, toUpdate.getLastname());
            updateStatement.setDate(3, toUpdate.getBirthdate());
            updateStatement.setDouble(4, toUpdate.getMonthlyPay());

            updateStatement.executeUpdate();

            String selectQuery = "SELECT * FROM client WHERE id=CAST(? AS UUID)";
            PreparedStatement selectStatement = conn.prepareStatement(selectQuery);
            selectStatement.setString(1, toUpdate.getId());

            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                Client updatedClient = new Client();
                updatedClient.setId(String.valueOf(UUID.fromString(resultSet.getString("id"))));
                updatedClient.setFirstname(resultSet.getString("firstname"));
                updatedClient.setLastname(resultSet.getString("lastname"));
                updatedClient.setBirthdate(resultSet.getDate("birthdate"));
                updatedClient.setMonthlyPay(resultSet.getDouble("monthlypay"));

                System.out.println("client updated");
                return updatedClient;
            } else {
                throw new RuntimeException("Failed to retrieve updated client information.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Client delete(Client toDelete) {
        return null;
    }

    public Client deleteClient(String toDelete) {
        try {
            String query = "delete FROM client WHERE id=CAST(? AS UUID)";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(UUID.fromString(toDelete)));
            preparedStatement.executeUpdate();

            return getOne(toDelete);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Client getOne(Client one) throws PropertyNotFoundException {
        return null;
    }


    public Client getOne(String id) throws PropertyNotFoundException {
        try {
            String query = "SELECT * FROM client WHERE id=CAST(? AS UUID)";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(UUID.fromString(id)));

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Client client = new Client();
                client.setId(String.valueOf(UUID.fromString(resultSet.getString("id"))));
                client.setFirstname(resultSet.getString("firstname"));
                client.setLastname(resultSet.getString("lastname"));
                client.setBirthdate(resultSet.getDate("birthdate"));
                client.setMonthlyPay(resultSet.getDouble("monthlypay"));


                return client;
            } else {
                throw new PropertyNotFoundException("Client not found with id: " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
