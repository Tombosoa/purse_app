package com.example.PurseApp.Controller;

import com.example.PurseApp.Entity.Account;
import com.example.PurseApp.Repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @PutMapping("/creditAuthorization")
    public void updateCreditAuthorization(
            @RequestParam UUID id,
            @RequestParam double newCount
    ){
        accountRepository.updateCreditAuthorization(id, newCount);
    }

    @GetMapping("/{idClient}")
    public List<Account> getByClientId(
            @PathVariable UUID idClient
    ){
        return accountRepository.getByClientId(idClient);
    }
}
