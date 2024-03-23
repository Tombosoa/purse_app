package com.example.PurseApp.Repository;

import com.example.PurseApp.DataBaseConnection;
import com.example.PurseApp.Entity.ApplyInterest;
import com.example.PurseApp.Entity.Interest;
import jakarta.el.PropertyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
public class ApplyInterestRepository implements CrudOperation<ApplyInterest>{
    String userName = System.getenv("DB_USERNAME");
    String password = System.getenv("DB_PASSWORD");
    String databaseName = System.getenv("DB_NAME");
    DataBaseConnection dbConnection = new DataBaseConnection(userName, password, databaseName);
    Connection conn = dbConnection.getConnection();
    public Statement statement;
    private final InterestRepository interestRepository;

    @Autowired
    public ApplyInterestRepository(InterestRepository interestRepository) {
        this.interestRepository = interestRepository;
    }

    @Override
    public List<ApplyInterest> findAll() {
        return null;
    }

    @Override
    public List<ApplyInterest> saveAll(List<ApplyInterest> toSave) {
        return null;
    }

    @Override
    public ApplyInterest save(ApplyInterest toSave) {
        int idApplyInterest = 0;
        try{
            String query = "INSERT INTO applyinterest (idAccount, idInterest, startDate, firstDue, actualDue) VALUES (CAST(? AS UUID),?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setObject(1, toSave.getIdAccount());
            preparedStatement.setInt(2, toSave.getIdInterest());
            preparedStatement.setObject(3, toSave.getStartDate());
            preparedStatement.setDouble(4, toSave.getFirstDue());
            preparedStatement.setDouble(5, toSave.getActualDue());

            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()){
                idApplyInterest = generatedKeys.getInt(1);
                toSave.setId(idApplyInterest);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return toSave;
    }

    @Override
    public ApplyInterest update(ApplyInterest toUpdate) {
        return null;
    }

    @Override
    public ApplyInterest delete(ApplyInterest toDelete) {
        return null;
    }

    @Override
    public ApplyInterest getOne(ApplyInterest one) throws PropertyNotFoundException {
        return null;
    }

    public ApplyInterest getOneById(int id){
        try {
            String query = "SELECT * FROM applyinterest where id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                ApplyInterest applyInterest = new ApplyInterest();
                applyInterest.setId(resultSet.getInt("id"));
                applyInterest.setIdAccount((UUID) resultSet.getObject("idAccount"));
                applyInterest.setIdInterest(resultSet.getInt("idInterest"));
                applyInterest.setStartDate(resultSet.getDate("startDate").toLocalDate());
                applyInterest.setFirstDue(resultSet.getDouble("firstDue"));
                applyInterest.setActualDue(resultSet.getDouble("actualDue"));

                return applyInterest;
            }else {
                throw new PropertyNotFoundException("ApplyInterest not found with id: " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateActualDue(int id, double newActualDue){
        try{
            String query = "UPDATE applyinterest SET actualDue = ? where id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setDouble(1, newActualDue);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ApplyInterest getByAccountId(UUID id){
        try {
            String query = "SELECT * FROM applyinterest where idaccount = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setObject(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                ApplyInterest applyInterest = new ApplyInterest();
                applyInterest.setId(resultSet.getInt("id"));
                applyInterest.setIdAccount((UUID) resultSet.getObject("idAccount"));
                applyInterest.setIdInterest(resultSet.getInt("idInterest"));
                applyInterest.setStartDate(resultSet.getDate("startDate").toLocalDate());
                applyInterest.setFirstDue(resultSet.getDouble("firstDue"));
                applyInterest.setActualDue(resultSet.getDouble("actualDue"));

                return applyInterest;
            }else {
                throw new PropertyNotFoundException("ApplyInterest not found");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateById(int id,LocalDate startDate, double actualDue, double firstDue){
        try {
            String query = "UPDATE applyinterest SET startdate = ?, firstdue = ?, actualdue = ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setObject(1, startDate);
            preparedStatement.setDouble(2, firstDue);
            preparedStatement.setDouble(3, actualDue);
            preparedStatement.setInt(4, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ApplyInterest getOneByInterestId(int id){
        try {
            String query = "SELECT * FROM applyinterest where idinterest = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                ApplyInterest applyInterest = new ApplyInterest();
                applyInterest.setId(resultSet.getInt("id"));
                applyInterest.setIdAccount((UUID) resultSet.getObject("idAccount"));
                applyInterest.setIdInterest(resultSet.getInt("idInterest"));
                applyInterest.setStartDate(resultSet.getDate("startDate").toLocalDate());
                applyInterest.setFirstDue(resultSet.getDouble("firstDue"));
                applyInterest.setActualDue(resultSet.getDouble("actualDue"));

                return applyInterest;
            }else {
                throw new PropertyNotFoundException("ApplyInterest not found");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
