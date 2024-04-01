package com.example.PurseApp.Controller;

import com.example.PurseApp.Entity.Transaction;
import com.example.PurseApp.Repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.*;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionController(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/all")
    public List<Transaction> getAllTransactions(){
        return transactionRepository.findAll();
    }

    @PostMapping("/new")
    public Transaction saveTransaction(@RequestBody Transaction toSave){
        return transactionRepository.save(toSave);
    }

    @GetMapping("/byAccountId")
    public Transaction getOneByAccountId(
            @RequestParam UUID id
            ){
        return transactionRepository.getByAccountId(id);
    }

    @GetMapping("/byClientId")
   public List<Transaction> getByClientId(
           @RequestParam UUID id
   ){
        return transactionRepository.findAllByClientId(id);
   }
    @GetMapping("/sum")
    public List<Map<String, Object>> sumIncomesAndExpenses(@RequestParam("startDate") String startDate,
                                                           @RequestParam("endDate") String endDate,
                                                           @RequestParam("accountId") String accountId) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        UUID accountIdUuid = UUID.fromString(accountId);
        try {
            return transactionRepository.sumIncomesAndExpenses(start, end, accountIdUuid);
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @PostMapping("/categorize")
    public String categorizeTransaction(
            @RequestParam String type,
            @RequestParam String name,
            @RequestParam int idTransaction
    ){
        return transactionRepository.categorizeTransaction(type, name, idTransaction);
    }


    @PostMapping("/categorizeSeveral")
    public String categorizeTransactions(
            @RequestParam String type,
            @RequestParam String name,
            @RequestParam List<Integer> idTransactions
    ){
        return transactionRepository.categorizeTransactions(type, name, idTransactions);
    }
}
