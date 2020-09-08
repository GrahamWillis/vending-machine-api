package com.druidwood.vendingmachineapi.actions;

import com.druidwood.vendingmachineapi.coinage.Coinage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ActionsController {
    @GetMapping(path = "/get")
    public Coinage.Coin get() {
        return Coinage.Coin.FIFTY_PENCE;
    }

    @PutMapping(path = "/insert", )
    public void insertCoin(@RequestBody Coinage.Coin coin) {
        //code
    }
}
