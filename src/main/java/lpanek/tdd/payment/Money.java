package lpanek.tdd.payment;

import java.util.Objects;

public class Money {

    private int wholes;
    private int pennies;

    public Money(int wholes, int pennies) {
        this.wholes = wholes;
        this.pennies = pennies;
    }

    public int getWholes() {
        return 0;
    }

    public int getPennies() {
        return 0;
    }

    public String getCurrencyCode() {
        return null;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object.getClass() != this.getClass()) {
            return false;
        }

        Money other = (Money) object;

        return (other.wholes == this.wholes)
                && (other.pennies == this.pennies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wholes, pennies);
    }
}
