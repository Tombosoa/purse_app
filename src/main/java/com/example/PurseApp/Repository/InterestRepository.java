package com.example.PurseApp.Repository;

import com.example.PurseApp.DataBaseConnection;
import com.example.PurseApp.Entity.Interest;
import jakarta.el.PropertyNotFoundException;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class InterestRepository implements CrudOperation<Interest>{
    String userName = System.getenv("DB_USERNAME");
    String password = System.getenv("DB_PASSWORD");
    String databaseName = System.getenv("DB_NAME");
    DataBaseConnection dbConnection = new DataBaseConnection(userName, password, databaseName);
    Connection conn = dbConnection.getConnection();
    public Statement statement;

    @Override
    public List<Interest> findAll() {
        List<Interest> interestList = new ArrayList<>();
        try(Statement statement = conn.createStatement()){
            String query = "SELECT * FROM interest";
            try(ResultSet resultSet = statement.executeQuery(query)){
                while(resultSet.next()){
                    int id = resultSet.getInt("id");
                    double counts = resultSet.getDouble("counts");
                    int dayGone = resultSet.getInt("daygone");

                    Interest interest = new Interest(id, counts, dayGone);
                    interestList.add(interest);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return interestList;
    }

    @Override
    public List<Interest> saveAll(List<Interest> toSave) {
        return null;
    }

    @Override
    public Interest save(Interest toSave) {
        int idInterest = 0;
        try{
            String query = "INSERT INTO interest (counts, dayGone) values (?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setDouble(1, toSave.getCounts());
            preparedStatement.setInt(2, toSave.getDayGone());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()){
                idInterest = generatedKeys.getInt(1);
                toSave.setId(idInterest);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return toSave;
    }

    @Override
    public Interest update(Interest toUpdate) {
        return null;
    }

    @Override
    public Interest delete(Interest toDelete) {
        return null;
    }

    @Override
    public Interest getOne(Interest one) throws PropertyNotFoundException {
        return null;
    }

    public Interest getOneById(int id){
        try{
            String query = "SELECT * FROM interest WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                Interest interest = new Interest();
                interest.setId(resultSet.getInt("id"));
                interest.setCounts(resultSet.getDouble("counts"));
                interest.setDayGone(resultSet.getInt("dayGone"));

                return interest;
            }else {
                throw new PropertyNotFoundException("Interest not found with id: " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateInterestCount(int id, double count){
        try{
            String query = "UPDATE interest SET counts = ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setDouble(1, count);
            preparedStatement.setInt(2, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void  updateInterestDayGone(int id, int newDayGone){
        try{
            String query = "UPDATE interest SET dayGone = ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, newDayGone);
            preparedStatement.setInt(2, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
