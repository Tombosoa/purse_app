package com.example.PurseApp.Functionnality;

import com.example.PurseApp.DataBaseConnection;
import com.example.PurseApp.Entity.*;
import com.example.PurseApp.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class Functionality {
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
    private final ClientRepository clientRepository;

    @Autowired
    public Functionality(AccountRepository accountRepository, TransactionRepository transactionRepository, TransferHistoryRepository transferHistoryRepository, CategoryRepository categoryRepository, ClientRepository clientRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.transferHistoryRepository = transferHistoryRepository;
        this.categoryRepository = categoryRepository;
        this.clientRepository = clientRepository;
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
        transaction.setStatus(false);
        transaction.setSituation("PENDING");
        transaction.setLabel(supplyBody.getLabel());


        if (supplyBody.getAction().equals("Loan") && creditAuthorization && actualBalance == 0) {
                transaction.setType("Loan");
                transaction.setDescription("make loan");
                Category category = categoryRepository.getByTypeAndName(supplyBody.getAction(), supplyBody.getActionName());
                transaction.setIdCategory(category.getId());
                if(account.getMonthlyPay() / 3 >= actualBalance - supplyBody.getSupplyAmount()){
                    Transaction response = transactionRepository.save(transaction);
                    idTransaction = response.getId();
                }else{
                    return 0;
                }
        } else if (supplyBody.getAction().equals("Outgoing") && !creditAuthorization) {
            if (actualBalance == 0) {
                return 0;
            } else if (actualBalance - supplyBody.getSupplyAmount() <= 0 ) {
                return 0;
            }else if (actualBalance - supplyBody.getSupplyAmount() >= 0) {
                transaction.setType("Outgoing");
                transaction.setDescription("Debited account");
                Category category = categoryRepository.getByTypeAndName(supplyBody.getAction(), supplyBody.getActionName());
                transaction.setIdCategory(category.getId());
                Transaction response = transactionRepository.save(transaction);
                idTransaction = response.getId();
            }
        } else if (supplyBody.getAction().equals("Incoming")) {
            transaction.setType("Incoming");
            transaction.setDescription("Credited account");
            Category category = categoryRepository.getByTypeAndName(supplyBody.getAction(), supplyBody.getActionName());
            transaction.setIdCategory(category.getId());
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
                idTransactionCredited = makeSupply(new SupplyBody("Incoming", amount, idAccountCredited, "Other"));
                idTransactionDebited = makeSupply(new SupplyBody("Outgoing", amount, idAccountDebited, "Other"));

                transactionRepository.updateEffectiveDateById(idTransactionCredited);

                TransferHistory transferHistory = new TransferHistory();
                transferHistory.setIdTransactionDebited(idTransactionDebited);
                transferHistory.setIdTransactionCredited(idTransactionCredited);
                return transferHistoryRepository.save(transferHistory);
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

    @Scheduled(cron = "0 * * * * *")
    public void balanceValueScheduled() throws SQLException {
        List<Transaction> transactions = transactionRepository.findAll();
        for (Transaction transaction:transactions){
            if(Objects.equals(transaction.getEffectiveDate(), LocalDate.now()) && !transaction.isStatus() && transaction.getSituation().equals("PENDING")){
                transactionRepository.updateStatusById(transaction.getId());
                UUID idAccount = UUID.fromString(transaction.getIdAccount());
                double actualBalance = accountRepository.getOneById(idAccount).getBalance();
                switch (transaction.getType()) {
                    case "Outgoing", "Loan" -> accountRepository.updateById(idAccount, actualBalance - transaction.getAmount());
                    case "Incoming" -> accountRepository.updateById(idAccount, actualBalance + transaction.getAmount());
                }
            }
        }
    }
}
