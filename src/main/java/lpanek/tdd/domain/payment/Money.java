package lpanek.tdd.domain.payment;

import java.util.*;

import lpanek.tdd.domain.payment.ex.MoneyException;

public class Money {

    public static final Money ZERO = new Money(0, 0);

    private final static String CURRENCY_SYMBOL = Currency.getInstance(new Locale("pl", "PL")).getSymbol();

    private int wholes;
    private int pennies;

    public Money(int wholes, int pennies) throws MoneyException {
        validateWholesAndPennies(wholes, pennies);
        this.wholes = wholes;
        this.pennies = pennies;
    }

    public Money plus(Money addend) {
        int totalPenniesInSum = this.calculateTotalPennies() + addend.calculateTotalPennies();
        return ofTotalPennies(totalPenniesInSum);
    }

    public Money minus(Money subtrahend) throws MoneyException {
        int totalPenniesInDifference = this.calculateTotalPennies() - subtrahend.calculateTotalPennies();
        if (totalPenniesInDifference < 0) {
            throw new MoneyException("Cannot subtract greater money from lesser money.");
        }
        return ofTotalPennies(totalPenniesInDifference);
    }

    public Money times(int multiplier) throws MoneyException {
        if (multiplier < 0) {
            throw new MoneyException("Cannot multiply by negative number.");
        }
        int totalPenniesInProduct = this.calculateTotalPennies() * multiplier;
        return ofTotalPennies(totalPenniesInProduct);
    }

    public boolean isGreaterThan(Money other) {
        return false;
    }

    public boolean isGreaterOrEqualTo(Money other) {
        return false;
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
        return String.format("%s=[%d.%02d %s]", getClass().getSimpleName(), wholes, pennies, CURRENCY_SYMBOL);
    }

    private void validateWholesAndPennies(int wholes, int pennies) throws MoneyException {
        List<String> violations = new LinkedList<>();
        if (wholes < 0) {
            violations.add("Wholes must not be negative.");
        }
        if (pennies < 0) {
            violations.add("Pennies must not be negative.");
        } else if (pennies > 100) {
            violations.add("Pennies must not be greater than 100.");
        }

        if (!violations.isEmpty()) {
            throw new MoneyException(String.join(" ", violations));
        }
    }

    private int calculateTotalPennies() {
        return wholes * 100 + pennies;
    }

    private Money ofTotalPennies(int totalPennies) {
        return new Money(totalPennies / 100, totalPennies % 100);
    }
}
