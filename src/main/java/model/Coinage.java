package model;

import com.fasterxml.jackson.annotation.JsonValue;

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
        private final short val;
        Coin(short val) {
            this.val = val;
        }

        @JsonValue
        public String getName() {
            return this.name();
        }
        public short getVal() {
            return val;
        }
    }

    public static final Comparator<Coin> simpleComparator =
            Comparator.comparingInt(Coin::getVal);
}
