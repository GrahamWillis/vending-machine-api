package com.druidwood.vendingmachineapi.machine;

import com.druidwood.vendingmachineapi.coinage.Coinage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Coinbox {
    Map<Coinage.Coin, Integer> machineBalance = Collections.synchronizedMap(new HashMap<Coinage.Coin, Integer>());

    public void credit(Coinage.Coin coin) {
        Integer count = machineBalance.putIfAbsent(coin, 1);
        if (count != null) {
            machineBalance.put(coin, count + 1);
        }
    }

    public boolean debit(Coinage.Coin coin) {
        Integer count = machineBalance.get(coin);
        if (count > 0) {
            machineBalance.put(coin, count - 1);
            return true;
        }

        return false;
    }

    public Integer getValue() {
        return machineBalance.values()
                .stream()
                .mapToInt(Integer::intValue).sum();
    }
}
