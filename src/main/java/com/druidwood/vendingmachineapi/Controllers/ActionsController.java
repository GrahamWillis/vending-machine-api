package com.druidwood.vendingmachineapi.Controllers;

import com.druidwood.vendingmachineapi.services.Machine;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Coinage;
import model.InsertMsg;
import model.InsertPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ActionsController {
    private final Machine machine;

    @Autowired
    public ActionsController(Machine machine) {
        this.machine = machine;
    }

    @GetMapping(path = "/float")
    public Integer get() {
        return machine.getMachineFloat();
    }

    @PostMapping(path = "/float", consumes = "application/json", produces = "application/json")
    public Integer setFloat(@RequestBody String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Integer> floatMsg = objectMapper.readValue(json, Map.class);
        return machine.addFloat(floatMsg);
    }

    @PostMapping(path = "/insert", consumes = "application/json", produces = "application/json")
    public InsertMsg insert(@RequestBody String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        InsertPayload payload = objectMapper.readValue(json, InsertPayload.class);
        Map<Coinage.Coin, Integer> insertedCoinsMap = payload.getCoinsInserted()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(e -> Coinage.Coin.valueOf(e.getKey()), Map.Entry::getValue));
        Integer amountInserted = machine.calculateValue(insertedCoinsMap);

        if (amountInserted < payload.getForAmount()) {
            return new InsertMsg(InsertMsg.INSUFFICIENT_INSERTED);
        } else if (payload.getForAmount() > machine.getMachineFloat()) {
            return new InsertMsg(InsertMsg.INSUFFICIENT_FLOAT);
        } else {
            // Insert the coins into the machine
            insertedCoinsMap.entrySet().forEach(e -> machine.credit(e.getKey(), e.getValue()));

            // Calculate change
            Map<Coinage.Coin, Integer> change;
            try {
                change = machine
                        .giveChangeToValue(amountInserted - payload.getForAmount());
            } catch (Machine.UnfulfillableOperation e) {
                // Give coins back
                return new InsertMsg(InsertMsg.CANNOT_GIVE_CHANGE, insertedCoinsMap);
            }

            return new InsertMsg(InsertMsg.CHANGE, change);
        }
    }
}
