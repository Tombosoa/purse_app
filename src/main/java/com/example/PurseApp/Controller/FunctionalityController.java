package com.example.PurseApp.Controller;

import com.example.PurseApp.Entity.AccountStatement;
import com.example.PurseApp.Entity.Category;
import com.example.PurseApp.Entity.SupplyBody;
import com.example.PurseApp.Entity.TransferHistory;
import com.example.PurseApp.Functionnality.Functionality;
import com.example.PurseApp.Repository.CategoryRepository;
import com.example.PurseApp.Repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@RestController
public class FunctionalityController {
    private final Functionality functionality;
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public FunctionalityController(Functionality functionality, TransactionRepository transactionRepository, CategoryRepository categoryRepository) {
        this.functionality = functionality;
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
    }

    @PostMapping("/makeSupply")
    public int makeSupply(@RequestBody SupplyBody supplyBody) throws SQLException {
        return functionality.makeSupply(supplyBody);
    }

    @PostMapping("/transfer")
    public TransferHistory makeTransfer(
            @RequestParam double amount,
            @RequestParam UUID idAccountDebited,
            @RequestParam UUID idAccountCredited
    ){
        return functionality.makeTransfer(amount, idAccountCredited,  idAccountDebited);
    }

    @PostMapping("/multiTransfer")
    public List<TransferHistory> multiTransfer(
            @RequestParam double amount,
            @RequestParam UUID idAccountDebited,
            @RequestParam List<UUID> idAccountCreditedList
    ){
        return functionality.multiTransfer(amount, idAccountDebited, idAccountCreditedList);
    }

    @PostMapping("/scheduledTransfer")
    public TransferHistory scheduledTransfer(
            @RequestParam double amount,
            @RequestParam UUID idAccountDebited,
            @RequestParam UUID idAccountCredited,
            @RequestParam LocalDate newEffectiveDate
            ){
        return functionality.scheduledTransfer(amount, idAccountCredited, idAccountDebited, newEffectiveDate);
    }

    @PostMapping("/canceledTransaction")
    public String canceledTransaction(
              @RequestParam int id
    ){
        return transactionRepository.canceledTransaction(id);
    }

    @GetMapping("/getStatement")
    public List<AccountStatement> getStatement(
           @RequestParam UUID idAccount
    ){
        return functionality.getAccountStatementByAccountId(idAccount);
    }
    @GetMapping("/categories/sum")
    public List<Category> sumAmountsByCategory(@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                               @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        try {
            return categoryRepository.sumAmountsByCategory(startDate, endDate);
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
