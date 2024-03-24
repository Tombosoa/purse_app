package com.example.PurseApp.Controller;

import com.example.PurseApp.Entity.AccountStatement;
import com.example.PurseApp.Entity.SupplyBody;
import com.example.PurseApp.Entity.TransferHistory;
import com.example.PurseApp.Functionnality.Functionality;
import com.example.PurseApp.Repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
public class FunctionalityController {
    private final Functionality functionality;
    private final TransactionRepository transactionRepository;

    @Autowired
    public FunctionalityController(Functionality functionality, TransactionRepository transactionRepository) {
        this.functionality = functionality;
        this.transactionRepository = transactionRepository;
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
}
