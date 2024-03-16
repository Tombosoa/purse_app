package com.example.PurseApp.Controller;

import com.example.PurseApp.Entity.Account;
import com.example.PurseApp.Repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("account")
public class AccountController {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @GetMapping("/all")
    public List<Account> getAll(){
        return accountRepository.findAll();
    }

    @PostMapping("/new")
    public Account newAccount(@RequestBody Account toSave){
        return accountRepository.save(toSave);
    }
}
