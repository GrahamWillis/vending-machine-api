package com.druidwood.vendingmachineapi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@NoArgsConstructor
@Getter @Setter
public class InsertPayload {
    private Map<String, Integer> coinsInserted;
    private Integer forAmount;
}