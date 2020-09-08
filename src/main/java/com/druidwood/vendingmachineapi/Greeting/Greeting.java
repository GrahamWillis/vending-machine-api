package com.druidwood.vendingmachineapi.Greeting;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Greeting {
    final private long id;
    final private String content;

    public Greeting(long id, String content) {
        this.id = id;
        this.content = content;
    }
}
