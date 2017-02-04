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

    public Money add(Money addend) {
        int totalPenniesInSum = this.calculateTotalPennies() + addend.calculateTotalPennies();
        return ofTotalPennies(totalPenniesInSum);
    }

    public Money subtract(Money subtrahend) {
        int totalPenniesInDifference = this.calculateTotalPennies() - subtrahend.calculateTotalPennies();
        return ofTotalPennies(totalPenniesInDifference);
    }

    public Money multiplyBy(int multiplier) {
        int totalPenniesInProduct = this.calculateTotalPennies() * multiplier;
        return ofTotalPennies(totalPenniesInProduct);
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

    @Override
    public String toString() {
        return String.format("%s=[wholes=%d, pennies=%d, currencySymbol='%s']",
                Money.class.getSimpleName(), wholes, pennies, CURRENCY_SYMBOL);
    }

    private int calculateTotalPennies() {
        return wholes * 100 + pennies;
    }

    private Money ofTotalPennies(int totalPennies) {
        return new Money(totalPennies / 100, totalPennies % 100);
    }
}
