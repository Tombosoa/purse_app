package com.example.PurseApp.Repository;

import com.example.PurseApp.DataBaseConnection;
import com.example.PurseApp.Entity.Category;
import com.example.PurseApp.Entity.Transaction;
import jakarta.el.PropertyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.*;

@Component
public class TransactionRepository implements CrudOperation<Transaction>{
    String userName = System.getenv("DB_USERNAME");
    String password = System.getenv("DB_PASSWORD");
    String databaseName = System.getenv("DB_NAME");
    DataBaseConnection dbConnection = new DataBaseConnection(userName, password, databaseName);
    Connection conn = dbConnection.getConnection();
    public Statement statement;
    private final CategoryRepository categoryRepository;

    @Autowired
    public TransactionRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

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
                    LocalDate registrationDate = result.getDate("registration_date").toLocalDate();
                    LocalDate effectiveDate = result.getDate("effective_date").toLocalDate();
                    double amount = result.getDouble("amount");
                    boolean status = result.getBoolean("status");
                    String reference = result.getString("reference");
                    int idCategory = result.getInt("id_category");
                    String idAccount = String.valueOf(UUID.fromString(result.getString("id_account")));
                    String label = result.getString("label");
                    String situation = result.getString("situation");

                    Transaction transaction = new Transaction(id, type, description, registrationDate, effectiveDate, amount, status, reference, idCategory, idAccount, label, situation);
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
            String query = "INSERT INTO transaction (type, description, effective_date, amount, status, reference, id_category, id_account, label, situation) values (?,?,?,?,?,?,?,CAST(? AS UUID), ?, ?)";
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
                transaction.setRegistrationDate(resultSet.getDate("registration_date").toLocalDate());
                transaction.setEffectiveDate(resultSet.getDate("effective_date").toLocalDate());
                transaction.setAmount(resultSet.getDouble("amount"));
                transaction.setStatus(resultSet.getBoolean("status"));
                transaction.setReference(resultSet.getString("reference"));
                transaction.setIdCategory(resultSet.getInt("id_category"));
                transaction.setIdAccount(resultSet.getString("id_account"));
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
            String query = "SELECT * FROM transaction WHERE id_account = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setObject(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                Transaction transaction = new Transaction();
                transaction.setId(resultSet.getInt("id"));
                transaction.setType(resultSet.getString("type"));
                transaction.setDescription(resultSet.getString("description"));
                transaction.setRegistrationDate(resultSet.getDate("registration_date").toLocalDate());
                transaction.setEffectiveDate(resultSet.getDate("effective_date").toLocalDate());
                transaction.setAmount(resultSet.getDouble("amount"));
                transaction.setStatus(resultSet.getBoolean("status"));
                transaction.setReference(resultSet.getString("reference"));
                transaction.setIdCategory(resultSet.getInt("id_category"));
                transaction.setIdAccount(resultSet.getString("id_account"));
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

    public List<Transaction> findAllByClientId(UUID id) {
        List<Transaction> transactionList = new ArrayList<>();
        try (PreparedStatement statement = conn.prepareStatement("select * from transaction inner join account on account.id = transaction.id_account inner join client on client.id = account.id_client where client.id = ?")) {
            statement.setObject(1, id);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    int ids = result.getInt("id");
                    String type = result.getString("type");
                    String description = result.getString("description");
                    LocalDate registrationDate = result.getDate("registration_date").toLocalDate();
                    LocalDate effectiveDate = result.getDate("effective_date").toLocalDate();
                    double amount = result.getDouble("amount");
                    boolean status = result.getBoolean("status");
                    String reference = result.getString("reference");
                    int idCategory = result.getInt("id_category");
                    String idAccount = String.valueOf(UUID.fromString(result.getString("id_account")));
                    String label = result.getString("label");
                    String situation = result.getString("situation");

                    Transaction transaction = new Transaction(ids,type, description, registrationDate, effectiveDate, amount, status, reference, idCategory, idAccount, label, situation);
                    transactionList.add(transaction);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching transaction from the database", e);
        }
        return transactionList;
    }

    public void updateEffectiveDateById(int id){
        try{
            String query = "UPDATE transaction SET effective_date= ? WHERE id = ?";
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
            String query = "UPDATE transaction SET effective_date = ? WHERE id  = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setObject(1, newEffectiveDate);
            preparedStatement.setInt(2, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Map<String, Object>> sumIncomesAndExpenses(LocalDate startDate, LocalDate endDate, UUID accountId) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<>();
        String sql = "SELECT * FROM SumIncomesAndExpenses(?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, startDate);
            stmt.setObject(2, endDate);
            stmt.setObject(3, accountId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> result = new HashMap<>();
                    result.put("month", rs.getInt("month"));
                    result.put("year", rs.getInt("year"));
                    result.put("Income", rs.getDouble("Income"));
                    result.put("Expense", rs.getDouble("Expense"));
                    results.add(result);
                }
            }
        }
        return results;
    }


    public String categorizeTransaction(String type, String name, int idTransaction){
        Category category = categoryRepository.getByTypeAndName(type, name);
        try{
            String query = "UPDATE transaction SET id_category = ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, category.getId());
            preparedStatement.setInt(2, idTransaction);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                return "Categorization successfully done";
            } else {
                return "No rows were updated";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String categorizeTransactions(String type, String name, List<Integer> idTransactions) {
        Category category = categoryRepository.getByTypeAndName(type, name);
        try {
            conn.setAutoCommit(false);

            String query = "UPDATE transaction SET id_category = ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);

            for (int idTransaction : idTransactions) {
                preparedStatement.setInt(1, category.getId());
                preparedStatement.setInt(2, idTransaction);
                preparedStatement.addBatch();
            }

            int[] rowsAffected = preparedStatement.executeBatch();
            conn.commit();

            return "Categorization successfully done for " + rowsAffected.length + " transactions";
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException(e);
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
