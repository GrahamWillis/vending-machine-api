package com.druidwood.vendingmachineapi.services;

import com.druidwood.vendingmachineapi.model.Coinage;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Machine {
    private static final Map<Coinage.Coin, Integer> machineBalance = Collections.synchronizedMap(new HashMap<>());

    public static class UnfulfillableOperation extends Exception {
        public UnfulfillableOperation(String errorMessage) {
            super(errorMessage);
        }
    }

    // Add float
    public static Integer addFloat(Map<String, Integer> floatMap) {
        floatMap.forEach((key, value) -> credit(Coinage.Coin.valueOf(key), value));
        return calculateValue(machineBalance);
    }

    public static void credit(Coinage.Coin coin, Integer qty) {
        Integer count = machineBalance.putIfAbsent(coin, qty);
        if (count != null) {
            machineBalance.put(coin, count + qty);
        }
    }

    public static boolean debit(Coinage.Coin coin, Integer qty) {
        Integer count = machineBalance.get(coin);
        if (count != null && count > 0) {
            machineBalance.put(coin, count - qty);
            return true;
        }

        return false;
    }

    public static Integer getMachineFloat() {
        return calculateValue(machineBalance);
    }

    public static Integer calculateValue(Map<Coinage.Coin, Integer> coinIntegerMap) {
        return coinIntegerMap
                .entrySet()
                .stream()
                .mapToInt(e -> e.getKey().getVal() * e.getValue()).sum();
    }

    private static Integer calculateOutstanding(Integer outstanding,
                                                Map<Coinage.Coin, Integer> change,
                                                Set<Coinage.Coin> coins) throws UnfulfillableOperation {

        Coinage.Coin coin = coins.stream()
                .sorted(Coinage.simpleComparator.reversed())
                .filter(v -> v.getVal() <= outstanding)
                .findFirst().orElse(null);

        // Remove this coin from the set - it won't be evaluated again
        coins.remove(coin);

        // Insufficient coins in the machine to make up change
        if (coin == null) {
            throw new UnfulfillableOperation("Cannot give correct change");
        } else {
            int qty = outstanding / coin.getVal();

            // Give coin as change if found in machine
            if (debit(coin, qty)) {
                change.put(coin, qty);
                return qty * coin.getVal();
            }
        }

        return 0;
    }

    public Map<Coinage.Coin, Integer> giveChangeToValue(final Integer amount) throws UnfulfillableOperation {
        Map<Coinage.Coin, Integer> change = new HashMap<>();

        Set<Coinage.Coin> coins = Arrays.stream(Coinage.Coin.values())
                .collect(Collectors.toSet());

        // Find the largest coin less then the required remaining amount
        Integer outstanding = amount;
        while (outstanding > 0) {
            outstanding -= calculateOutstanding(outstanding, change, coins);
        }

        return change;
    }
}
