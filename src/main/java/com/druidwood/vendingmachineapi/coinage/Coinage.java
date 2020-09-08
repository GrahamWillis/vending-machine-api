package com.druidwood.vendingmachineapi.coinage;

import lombok.Getter;

import java.util.Comparator;

public class Coinage {
    public enum Coin {
        ONE_PENCE((short)1),
        TWO_PENCE((short)2),
        FIVE_PENCE((short)5),
        TEN_PENCE((short)10),
        TWENTY_PENCE((short)20),
        FIFTY_PENCE((short)50),
        ONE_POUND((short)100),
        TWO_POUND((short)200),
        FIVE_POUND((short)500);
        private final short value;
        Coin(short value) {
            this.value = value;
        }
        public short getValue() {
            return value;
        }
    }

    public static Comparator<Coin> simpleComparator =
            Comparator.comparingInt(Coin::getValue);
}
