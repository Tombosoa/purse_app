package com.example.PurseApp.Repository;

import com.example.PurseApp.DataBaseConnection;
import com.example.PurseApp.Entity.TransferHistory;
import jakarta.el.PropertyNotFoundException;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

@Component
public class TransferHistoryRepository implements CrudOperation<TransferHistory>{
    String userName = System.getenv("DB_USERNAME");
    String password = System.getenv("DB_PASSWORD");
    String databaseName = System.getenv("DB_NAME");
    DataBaseConnection dbConnection = new DataBaseConnection(userName, password, databaseName);
    Connection conn = dbConnection.getConnection();
    public Statement statement;
    @Override
    public List<TransferHistory> findAll() {
        return null;
    }

    @Override
    public List<TransferHistory> saveAll(List<TransferHistory> toSave) {

        return null;
    }

    @Override
    public TransferHistory save(TransferHistory toSave) {
        int id = 0;
        try{
            String query = "INSERT INTO transferHistory (idtransactioncredited, idtransactiondebited) values (?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, toSave.getIdTransactionCredited());
            preparedStatement.setInt(2, toSave.getIdTransactionDebited());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()){
                id = generatedKeys.getInt(1);
                toSave.setId(id);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return toSave;
    }

    @Override
    public TransferHistory update(TransferHistory toUpdate) {
        return null;
    }

    @Override
    public TransferHistory delete(TransferHistory toDelete) {
        return null;
    }

    @Override
    public TransferHistory getOne(TransferHistory one) throws PropertyNotFoundException {
        return null;
    }
}
