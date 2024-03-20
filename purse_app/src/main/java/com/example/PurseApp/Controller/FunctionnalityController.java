package com.example.PurseApp.Controller;

import com.example.PurseApp.Entity.SupplyBody;
import com.example.PurseApp.Entity.TransferHistory;
import com.example.PurseApp.Functionnality.Functionnality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.UUID;

@RestController
public class FunctionnalityController {
    private final Functionnality functionnality;

    @Autowired
    public FunctionnalityController(Functionnality functionnality) {
        this.functionnality = functionnality;
    }

    @PostMapping("/makeSupply")
    public int makeSupply(@RequestBody SupplyBody supplyBody) throws SQLException {
        return functionnality.makeSupply(supplyBody);
    }

    @PostMapping("/transfer")
    public TransferHistory makeTransfer(
            @RequestParam double amount,
            @RequestParam UUID idAccountDebited,
            @RequestParam UUID idAccountCredited
    ){
        return functionnality.makeTransfer(amount, idAccountCredited,  idAccountDebited);
    }
}
