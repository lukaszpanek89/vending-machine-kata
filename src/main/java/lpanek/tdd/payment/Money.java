package lpanek.tdd.payment;

import java.util.*;

public class Money {

    private final static String CURRENCY_SYMBOL = Currency.getInstance(new Locale("pl", "PL")).getSymbol();

    private int wholes;
    private int pennies;

    public Money(int wholes, int pennies) {
        this.wholes = wholes;
        this.pennies = pennies;
    }

    public Money subtract(Money subtrahend) {
        int totalPenniesInDifference = this.calculateTotalPennies() - subtrahend.calculateTotalPennies();
        return new Money(totalPenniesInDifference / 100, totalPenniesInDifference % 100);
    }

    public int getWholes() {
        return wholes;
    }

    public int getPennies() {
        return pennies;
    }

    public String getCurrencySymbol() {
        return CURRENCY_SYMBOL;
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

    private int calculateTotalPennies() {
        return wholes * 100 + pennies;
    }
}
