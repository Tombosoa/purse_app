package com.example.PurseApp.Repository;

import com.example.PurseApp.DataBaseConnection;
import com.example.PurseApp.Entity.Transaction;
import jakarta.el.PropertyNotFoundException;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
                    String label = result.getString("label");
                    String situation = result.getString("situation");

                    Transaction transaction = new Transaction(id,type, description, registrationDate, effectiveDate, amount, status, reference, idCategory, idAccount, label, situation);
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
            String query = "INSERT INTO transaction (type, description, effectivedate, amount, status, reference, idcategory, idaccount, label, situation) values (?,?,?,?,?,?,?,CAST(? AS UUID), ?, ?)";
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
            preparedStatement.setString(9, toSave.getLabel());
            preparedStatement.setString(10, toSave.getSituation());
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

    public Transaction getOneById(int id){
        try {
            String query = "SELECT * FROM transaction WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                Transaction transaction = new Transaction();
                transaction.setId(resultSet.getInt("id"));
                transaction.setType(resultSet.getString("type"));
                transaction.setDescription(resultSet.getString("description"));
                transaction.setRegistrationDate(resultSet.getDate("registrationdate").toLocalDate());
                transaction.setEffectiveDate(resultSet.getDate("effectivedate").toLocalDate());
                transaction.setAmount(resultSet.getDouble("amount"));
                transaction.setStatus(resultSet.getBoolean("status"));
                transaction.setReference(resultSet.getString("reference"));
                transaction.setIdCategory(resultSet.getInt("idcategory"));
                transaction.setIdAccount(resultSet.getString("idaccount"));
                transaction.setLabel(resultSet.getString("label"));
                transaction.setSituation(resultSet.getString("situation"));

                return transaction;
            }else {
                throw new PropertyNotFoundException("not found");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Transaction getByAccountId(UUID id){
        try {
            String query = "SELECT * FROM transaction WHERE idaccount = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setObject(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                Transaction transaction = new Transaction();
                transaction.setId(resultSet.getInt("id"));
                transaction.setType(resultSet.getString("type"));
                transaction.setDescription(resultSet.getString("description"));
                transaction.setRegistrationDate(resultSet.getDate("registrationdate").toLocalDate());
                transaction.setEffectiveDate(resultSet.getDate("effectivedate").toLocalDate());
                transaction.setAmount(resultSet.getDouble("amount"));
                transaction.setStatus(resultSet.getBoolean("status"));
                transaction.setReference(resultSet.getString("reference"));
                transaction.setIdCategory(resultSet.getInt("idcategory"));
                transaction.setIdAccount(resultSet.getString("idaccount"));
                transaction.setLabel(resultSet.getString("label"));
                transaction.setSituation(resultSet.getString("situation"));

                return transaction;
            }else {
                throw new PropertyNotFoundException("not found");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateEffectiveDateById(int id){
        try{
            String query = "UPDATE transaction SET effectivedate= ? WHERE id = ?";
            Transaction transaction = getOneById(id);
            LocalDateTime registrationDate = transaction.getRegistrationDate().atTime(LocalTime.now());
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setObject(1, registrationDate.plusDays(2));
            preparedStatement.setInt(2, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateStatusById(int id){
        try{
            String query = "UPDATE transaction SET status = true ,situation = 'SUCCESS' WHERE id = ?";
            Transaction transaction = getOneById(id);
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String canceledTransaction(int id){
        String situation = getOneById(id).getSituation();
        try{
            String query = "UPDATE transaction SET situation = 'CANCELED' WHERE id = ?";
            if (situation.equals("SUCCESS")){
                return "Can't canceled this transaction";
            }else {
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setInt(1, id);

                preparedStatement.executeUpdate();
                return "Transaction canceled";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateEffectiveDate(int id, LocalDate newEffectiveDate){
        try{
            String query = "UPDATE transaction SET effectiveDate = ? WHERE id  = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setObject(1, newEffectiveDate);
            preparedStatement.setInt(2, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
