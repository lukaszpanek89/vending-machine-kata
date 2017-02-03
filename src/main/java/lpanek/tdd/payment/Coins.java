package lpanek.tdd.payment;

public class Coins {

    public Coins() {

    }

    public Coins(Coin... coins) {

    }

    public Coins add(Coin... coins) {
        return null;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object.getClass() != Coins.class) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
