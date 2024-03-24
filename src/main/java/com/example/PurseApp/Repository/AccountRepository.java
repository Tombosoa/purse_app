package com.example.PurseApp.Repository;

import com.example.PurseApp.DataBaseConnection;
import com.example.PurseApp.Entity.Account;
import com.example.PurseApp.Entity.ApplyInterest;
import com.example.PurseApp.Entity.Interest;
import jakarta.el.PropertyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class AccountRepository implements CrudOperation<Account>{
    String userName = System.getenv("DB_USERNAME");
    String password = System.getenv("DB_PASSWORD");
    String databaseName = System.getenv("DB_NAME");
    DataBaseConnection dbConnection = new DataBaseConnection(userName, password, databaseName);
    Connection conn = dbConnection.getConnection();
    public Statement statement;
    private final InterestRepository interestRepository;
    private final ApplyInterestRepository applyInterestRepository;

    @Autowired
    public AccountRepository(InterestRepository interestRepository, ApplyInterestRepository applyInterestRepository) {
        this.interestRepository = interestRepository;
        this.applyInterestRepository = applyInterestRepository;
    }

    @Override
    public List<Account> findAll() {
        List<Account> accountList = new ArrayList<>();
        try (Statement statement = conn.createStatement()){
            String query = "select account.id as id, firstname, lastname, birthdate, monthlypay, balance, creditauthorization, bank.name, client.id as idclient from client inner join account on client.id = account.idclient inner join bank on bank.id = account.idbank";
            try (ResultSet resultSet = statement.executeQuery(query)){
                while (resultSet.next()){
                    String id = resultSet.getString("id");
                    String firstname = resultSet.getString("firstname");
                    String lastname = resultSet.getString("lastname");
                    Date birthdate = resultSet.getDate("birthdate");
                    double monthlyPay = resultSet.getDouble("monthlypay");
                    double balance = resultSet.getDouble("balance");
                    boolean creditAuthorization = resultSet.getBoolean("creditauthorization");
                    String name = resultSet.getString("name");
                    String idClient = resultSet.getString("idclient");

                    Account account = new Account(id, firstname, lastname, birthdate, monthlyPay, balance, creditAuthorization, name, idClient);
                    accountList.add(account);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return accountList;
    }

    @Override
    public List<Account> saveAll(List<Account> toSave) {
        return null;
    }

    @Override
    public Account save(Account toSave) {
        try{
            String query = "INSERT INTO account (balance, idclient, idbank) VALUES (?,CAST(? AS UUID),?)";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setDouble(1, toSave.getBalance());
            preparedStatement.setString(2, String.valueOf(UUID.fromString(toSave.getIdClient())));
            preparedStatement.setInt(3, toSave.getIdBank());
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return toSave;
    }

    @Override
    public Account update(Account toUpdate) {
        return null;
    }

    @Override
    public Account delete(Account toDelete) {
        return null;
    }

    @Override
    public Account getOne(Account one) throws PropertyNotFoundException {
        return null;
    }

    public Account updateById(UUID idAccount, double amount) throws SQLException {
        String updateAccount = "UPDATE account SET balance = ? WHERE id = ?";
        PreparedStatement newStatement = conn.prepareStatement(updateAccount);
        newStatement.setDouble(1, amount);
        newStatement.setObject(2, idAccount);
        newStatement.executeUpdate();
        return getOneById(idAccount);
    }

    public Account getOneById(UUID id){
        try{
            String query = "SELECT * FROM account WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setObject(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                Account account = new Account();
                account.setId(resultSet.getString("id"));
                account.setBalance(resultSet.getDouble("balance"));
                account.setCreditAuthorization(resultSet.getBoolean("creditauthorization"));
                account.setIdBank(resultSet.getInt("idbank"));
                account.setIdClient(resultSet.getString("idclient"));

                return account;
            } else {
                throw new PropertyNotFoundException("Account not found with id: " + id);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateBalanceById(UUID id, double newBalance){
        try{
            String query = "UPDATE account SET balance = ? WHERE id = ?";
            Account account = getOneById(id);
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setDouble(1, account.getBalance() + newBalance);
            preparedStatement.setObject(2, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateCreditAuthorization(UUID id, double newCount){
        try{
            String query = "UPDATE account SET creditauthorization = true WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            Account account = getOneById(id);
            if(!account.isCreditAuthorization()){
                preparedStatement.setObject(1, id);

                preparedStatement.executeUpdate();
                Interest interest = new Interest();
                interest.setCounts(0.0);
                interest.setDayGone(0);
                Interest interestSaved = interestRepository.save(interest);
                int idInterest = interestSaved.getId();
                ApplyInterest applyInterest = new ApplyInterest();
                applyInterest.setIdAccount(id);
                applyInterest.setActualDue(0.0);
                applyInterest.setIdInterest(idInterest);
                applyInterest.setStartDate(null);
                applyInterest.setFirstDue(0.0);
                applyInterestRepository.save(applyInterest);

                interestRepository.updateInterestCount(idInterest, newCount);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
