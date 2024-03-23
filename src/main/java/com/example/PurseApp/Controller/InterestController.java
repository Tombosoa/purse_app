package com.example.PurseApp.Controller;

import com.example.PurseApp.Entity.Interest;
import com.example.PurseApp.Repository.InterestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("interest")
public class InterestController {
    private final InterestRepository interestRepository;

    @Autowired
    public InterestController(InterestRepository interestRepository) {
        this.interestRepository = interestRepository;
    }

    @PostMapping("/new")
    public Interest save(
            @RequestBody Interest toSave
    ){
        return interestRepository.save(toSave);
    }

    @GetMapping("/getOne")
    public Interest getOneById(
            @RequestParam int id
    ){
        return interestRepository.getOneById(id);
    }
}
