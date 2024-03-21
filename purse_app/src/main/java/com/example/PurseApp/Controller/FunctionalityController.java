package com.example.PurseApp.Controller;

import com.example.PurseApp.Entity.SupplyBody;
import com.example.PurseApp.Entity.TransferHistory;
import com.example.PurseApp.Functionnality.Functionality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.UUID;

@RestController
public class FunctionalityController {
    private final Functionality functionality;

    @Autowired
    public FunctionalityController(Functionality functionality) {
        this.functionality = functionality;
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
}
