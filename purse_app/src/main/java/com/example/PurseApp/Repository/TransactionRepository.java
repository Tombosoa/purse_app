package com.example.PurseApp.Repository;

import com.example.PurseApp.DataBaseConnection;
import com.example.PurseApp.Entity.Transaction;
import jakarta.el.PropertyNotFoundException;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class TransactionRepository implements CrudOperation<Transaction>{
    String userName = System.getenv("DB_USERNAME");
    String password = System.getenv("DB_PASSWORD");
    String databaseName = System.getenv("DB_NAME");
    DataBaseConnection dbConnection = new DataBaseConnection(userName, password, databaseName);
    Connection conn = dbConnection.getConnection();
    public Statement statement;
    @Override
    public List<Transaction> findAll() {
        List<Transaction> transactionList = new ArrayList<>();
        try (Statement statement = conn.createStatement()) {
            String query = "SELECT * from transaction ";
            try (ResultSet result = statement.executeQuery(query)) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String type = result.getString("type");
                    String description = result.getString("description");
                    LocalDate registrationDate = result.getDate("registrationdate").toLocalDate();
                    LocalDate effectiveDate = result.getDate("effectivedate").toLocalDate();
                    double amount = result.getDouble("amount");
                    boolean status = result.getBoolean("status");
                    String reference = result.getString("reference");
                    int idCategory = result.getInt("idcategory");
                    String idAccount = String.valueOf(UUID.fromString(result.getString("idaccount")));

                    Transaction transaction = new Transaction(id,type, description, registrationDate, effectiveDate, amount, status, reference, idCategory, idAccount);
                    transactionList.add(transaction);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching transaction from the database", e);
        }
        return transactionList;
    }

    @Override
    public List<Transaction> saveAll(List<Transaction> toSave) {
        return null;
    }

    @Override
    public Transaction save(Transaction toSave) {
        int idTransaction = 0;
        try{
            String query = "INSERT INTO transaction (type, description, effectivedate, amount, status, reference, idcategory, idaccount) values (?,?,?,?,?,?,?,CAST(? AS UUID))";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_ss");
            Date date = new Date();
            String dateFormatted = sdf.format(date);
            PreparedStatement preparedStatement = conn.prepareStatement(query,  Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, toSave.getType());
            preparedStatement.setString(2, toSave.getDescription());
            preparedStatement.setObject(3, toSave.getEffectiveDate());
            preparedStatement.setDouble(4, toSave.getAmount());
            preparedStatement.setBoolean(5, toSave.isStatus());
            preparedStatement.setString(6, "VIR_"+dateFormatted);
            preparedStatement.setInt(7, toSave.getIdCategory());
            preparedStatement.setString(8, String.valueOf(UUID.fromString(toSave.getIdAccount())));
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()){
                idTransaction = generatedKeys.getInt(1);
                toSave.setId(idTransaction);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return toSave;
    }

    @Override
    public Transaction update(Transaction toUpdate) {
        return null;
    }

    @Override
    public Transaction delete(Transaction toDelete) {
        return null;
    }

    @Override
    public Transaction getOne(Transaction one) throws PropertyNotFoundException {
        return null;
    }
}
