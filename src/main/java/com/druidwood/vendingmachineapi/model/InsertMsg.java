package com.druidwood.vendingmachineapi.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter @Setter
public class InsertMsg {
    public static final String INSUFFICIENT_INSERTED = "Payment insufficient";
    public static final String INSUFFICIENT_FLOAT = "Insufficient funds in machine";
    public static final String CHANGE = "Returning change";
    public static final String CANNOT_GIVE_CHANGE = "Insufficient coinage to give correct change";

    private Map<Coinage.Coin, Integer> change;
    private String message;

    public InsertMsg(String message) {
        this.message = message;
    }

    public InsertMsg(String message, Map<Coinage.Coin, Integer> change) {
        this.message = message;
        this.change = change;
    }
}