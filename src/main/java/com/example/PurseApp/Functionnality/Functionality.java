package com.example.PurseApp.Functionnality;

import com.example.PurseApp.DataBaseConnection;
import com.example.PurseApp.Entity.*;
import com.example.PurseApp.Repository.*;
import jakarta.el.PropertyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
    private final ApplyInterestRepository applyInterestRepository;
    private final InterestRepository interestRepository;

    @Autowired
    public Functionality(AccountRepository accountRepository, TransactionRepository transactionRepository, TransferHistoryRepository transferHistoryRepository, CategoryRepository categoryRepository, ClientRepository clientRepository, ApplyInterestRepository applyInterestRepository, InterestRepository interestRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.transferHistoryRepository = transferHistoryRepository;
        this.categoryRepository = categoryRepository;
        this.clientRepository = clientRepository;
        this.applyInterestRepository = applyInterestRepository;
        this.interestRepository = interestRepository;
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


        if (supplyBody.getAction().equals("Loan") && creditAuthorization && (actualBalance >= 0)) {
                transaction.setType("Loan");
                transaction.setDescription("make loan");
                Category category = categoryRepository.getByTypeAndName(supplyBody.getAction(), supplyBody.getActionName());
                transaction.setIdCategory(category.getId());
                if(account.getMonthlyPay() / 3 >= actualBalance - supplyBody.getSupplyAmount()){
                    Transaction response = transactionRepository.save(transaction);
                    idTransaction = response.getId();
                    ApplyInterest applyInterest = applyInterestRepository.getByAccountId(supplyBody.getIdAccount());
                    int idInterest = applyInterest.getIdInterest();
                    int idApplyInterest = applyInterest.getId();
                    Interest interest = interestRepository.getOneById(idInterest);
                    LocalDate startDate = LocalDate.now();
                    if(actualBalance == 0){
                        double actualDue = interest.getCounts() * interest.getDayGone() * supplyBody.getSupplyAmount();
                        applyInterestRepository.updateById(idApplyInterest, startDate, actualDue, supplyBody.getSupplyAmount());
                    }else if(actualBalance < supplyBody.getSupplyAmount()){
                        double firstDue = supplyBody.getSupplyAmount() - actualBalance;
                        double actualDue =  interest.getCounts() * interest.getDayGone() * firstDue;
                        applyInterestRepository.updateById(idApplyInterest, startDate, actualDue, firstDue);
                    }
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

    public List<TransferHistory> multiTransfer(double amount, UUID idAccountDebited, List<UUID> idAccountCreditedList){
        List<TransferHistory> result = new ArrayList<>();
        for (UUID idAccountCredited:idAccountCreditedList){
            TransferHistory res = makeTransfer(amount, idAccountCredited,idAccountDebited);
            result.add(res);
        }
        return result;
    }

    public TransferHistory scheduledTransfer(double amount, UUID idAccountCredited, UUID idAccountDebited, LocalDate newEffectiveDate){
        TransferHistory result = makeTransfer(amount, idAccountCredited, idAccountDebited);
        int idTransactionCredited = result.getIdTransactionCredited();
        int idTransactionDebited = result.getIdTransactionDebited();
        transactionRepository.updateEffectiveDate(idTransactionCredited, newEffectiveDate);
        transactionRepository.updateEffectiveDate(idTransactionDebited, newEffectiveDate);
        return result;
    }
    public List<AccountStatement> getAccountStatementByAccountId(UUID idAccount) {
        List<AccountStatement> accountStatements = new ArrayList<>();
        try (PreparedStatement preparedStatement = conn.prepareStatement(
                "select effectivedate, reference, label as motif, balance, " +
                        "case when description = 'Credited account' then amount else 0 end as creditMga, " +
                        "case when description = 'Debited account' then amount else 0 end as debitMga " +
                        "from transaction inner join account on account.id = transaction.idaccount where idaccount=?")) {
            preparedStatement.setObject(1, idAccount);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    LocalDate effectiveDate = resultSet.getDate("effectivedate").toLocalDate();
                    String reference = resultSet.getString("reference");
                    String motif = resultSet.getString("motif");
                    double creditMGA = resultSet.getDouble("creditMga");
                    double debitMGA = resultSet.getDouble("debitMga");
                    double balance = resultSet.getDouble("balance");
                    AccountStatement accountStatement = new AccountStatement(effectiveDate, reference, motif, creditMGA, debitMGA, balance);
                    accountStatements.add(accountStatement);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return accountStatements;
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

    @Scheduled(cron = "0 0 0 * * ?")
    public void countScheduled(){
        List<Interest> interests = interestRepository.findAll();
        for(Interest interest:interests){
            ApplyInterest applyInterest = applyInterestRepository.getOneByInterestId(interest.getId());
            LocalDate currentDate = LocalDate.now();
            LocalDate startDate = applyInterest.getStartDate();
            int dayGone = (int) ChronoUnit.DAYS.between(startDate, currentDate);
            interestRepository.updateInterestDayGone(interest.getId(), dayGone);
            if(interest.getDayGone() == 8){
                double actualCount = interest.getCounts();
                interestRepository.updateInterestCount(interest.getId(),actualCount + 0.02);
            }
            double actualDue = interest.getDayGone() * interest.getCounts() * applyInterest.getFirstDue() + applyInterest.getFirstDue();
            applyInterestRepository.updateActualDue(applyInterest.getId(), actualDue);
        }
    }
}
