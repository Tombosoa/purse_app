package com.example.PurseApp.Functionnality;

import com.example.PurseApp.DataBaseConnection;
import com.example.PurseApp.Entity.*;
import com.example.PurseApp.Repository.AccountRepository;
import com.example.PurseApp.Repository.CategoryRepository;
import com.example.PurseApp.Repository.TransactionRepository;
import com.example.PurseApp.Repository.TransferHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Component
public class Functionnality {
    String userName = System.getenv("DB_USERNAME");
    String password = System.getenv("DB_PASSWORD");
    String databaseName = System.getenv("DB_NAME");
    DataBaseConnection dbConnection = new DataBaseConnection(userName, password, databaseName);
    Connection conn = dbConnection.getConnection();
    public Statement statement;

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransferHistoryRepository transferHistoryRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public Functionnality(AccountRepository accountRepository, TransactionRepository transactionRepository, TransferHistoryRepository transferHistoryRepository, CategoryRepository categoryRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.transferHistoryRepository = transferHistoryRepository;
        this.categoryRepository = categoryRepository;
    }

    public int makeSupply(SupplyBody supplyBody) throws SQLException {
        int idTransaction = 0;
        Account account = accountRepository.getOneById(supplyBody.getIdAccount());
        boolean creditAuthorization = account.isCreditAuthorization();
        double actualBalance = account.getBalance();
        LocalDate date = LocalDate.now();
        Transaction transaction = new Transaction();
        transaction.setAmount(supplyBody.getSupplyAmount());
        transaction.setEffectiveDate(date);
        transaction.setIdAccount(supplyBody.getIdAccount().toString());
        transaction.setStatus(true);

        if (supplyBody.getAction().equals("Loan") && creditAuthorization) {
                accountRepository.updateById(supplyBody.getIdAccount(), actualBalance - supplyBody.getSupplyAmount());
                transaction.setType("Loan");
                transaction.setDescription("make loan");
                Category category = categoryRepository.getByTypeAndName(supplyBody.getAction(), supplyBody.getActionName());
                transaction.setIdCategory(category.getId());
                Transaction response = transactionRepository.save(transaction);
                idTransaction = response.getId();
        } else if (supplyBody.getAction().equals("Outgoing") && !creditAuthorization) {
            if (actualBalance == 0) {
                return 0;
            } else if (actualBalance - supplyBody.getSupplyAmount() <= 0 ) {
                return 0;
            }else if (actualBalance - supplyBody.getSupplyAmount() >= 0) {
                accountRepository.updateById(supplyBody.getIdAccount(), actualBalance - supplyBody.getSupplyAmount());
                transaction.setType("Outgoing");
                transaction.setDescription("Debited account");
                Category category = categoryRepository.getByTypeAndName(supplyBody.getAction(), supplyBody.getActionName());
                transaction.setIdCategory(category.getId());
                Transaction response = transactionRepository.save(transaction);
                idTransaction = response.getId();
            }
        } else if (supplyBody.getAction().equals("Incoming")) {
            accountRepository.updateById(supplyBody.getIdAccount(), actualBalance + supplyBody.getSupplyAmount());
            transaction.setType("Incoming");
            transaction.setDescription("Credited account");
            Category category = categoryRepository.getByTypeAndName(supplyBody.getAction(), supplyBody.getActionName());
            transaction.setIdCategory(category.getId());
            transactionRepository.save(transaction);
            Transaction response = transactionRepository.save(transaction);
            idTransaction = response.getId();
        }
        return idTransaction;
    }

    public TransferHistory makeTransfer(double amount, UUID idAccountCredited, UUID idAccountDebited) {
        try {
            Account accountDebited = accountRepository.getOneById(idAccountDebited);
            Account accountCredited = accountRepository.getOneById(idAccountCredited);
            int idTransactionDebited;
            int idTransactionCredited;

            if (accountCredited.getIdBank() != accountDebited.getIdBank()) {
                idTransactionCredited = makeSupply(new SupplyBody("Incoming", 0.0, idAccountCredited, "Other"));
                idTransactionDebited = makeSupply(new SupplyBody("Outgoing", amount, idAccountDebited, "Other"));

                transactionRepository.updateEffectiveDateById(idTransactionCredited);

                while (!verifyingDate(idTransactionCredited)) {
                    try {
                        Thread.sleep(120000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                return updateBalance(idAccountCredited, amount, idTransactionDebited, idTransactionCredited);
            } else {
                idTransactionCredited = makeSupply(new SupplyBody("Incoming", amount, idAccountCredited, "Other"));
                idTransactionDebited = makeSupply(new SupplyBody("Outgoing", amount, idAccountDebited, "Other"));
                TransferHistory transferHistory = new TransferHistory();
                transferHistory.setIdTransactionDebited(idTransactionDebited);
                transferHistory.setIdTransactionCredited(idTransactionCredited);
                return transferHistoryRepository.save(transferHistory);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean verifyingDate(int id){
        Transaction transaction = transactionRepository.getOneById(id);
        LocalDateTime currentDate = LocalDateTime.now();
        return currentDate.toLocalDate() == transaction.getEffectiveDate();
    }

    public TransferHistory updateBalance(UUID idAccountCredited, double amount, int idTransactionDebited, int idTransactionCredited) throws SQLException {
        accountRepository.updateById(idAccountCredited, amount);
        TransferHistory transferHistory = new TransferHistory();
        transferHistory.setIdTransactionDebited(idTransactionDebited);
        transferHistory.setIdTransactionCredited(idTransactionCredited);
        return transferHistoryRepository.save(transferHistory);
    }
}
