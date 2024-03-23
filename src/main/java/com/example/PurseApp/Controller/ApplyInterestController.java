package com.example.PurseApp.Controller;

import com.example.PurseApp.Entity.ApplyInterest;
import com.example.PurseApp.Repository.ApplyInterestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("applyInterest")
public class ApplyInterestController {
    private final ApplyInterestRepository applyInterestRepository;

    @Autowired
    public ApplyInterestController(ApplyInterestRepository applyInterestRepository) {
        this.applyInterestRepository = applyInterestRepository;
    }

    @PostMapping("/new")
    public ApplyInterest save(
            @RequestBody ApplyInterest toSave
    ){
        return applyInterestRepository.save(toSave);
    }

    @GetMapping("/getOne")
    public ApplyInterest getOneById(
            @RequestParam int id
    ){
        return applyInterestRepository.getOneById(id);
    }
}
