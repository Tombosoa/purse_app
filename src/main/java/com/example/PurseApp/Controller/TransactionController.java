package com.example.PurseApp.Controller;

import com.example.PurseApp.Entity.Transaction;
import com.example.PurseApp.Repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
}
