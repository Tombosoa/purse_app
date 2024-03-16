package com.example.PurseApp.Controller;

import com.example.PurseApp.Entity.Bank;
import com.example.PurseApp.Repository.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("bank")
@RestController
public class BankController {
    private final BankRepository bankRepository;

    @Autowired
    public BankController(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    @GetMapping("/all")
    public List<Bank> allBank(){
        return bankRepository.findAll();
    }

    @PostMapping("/new")
    public Bank newBank(@RequestBody Bank toSave){
        return bankRepository.save(toSave);
    }
}
