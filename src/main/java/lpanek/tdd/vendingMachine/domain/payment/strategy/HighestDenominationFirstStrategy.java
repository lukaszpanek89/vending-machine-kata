package lpanek.tdd.vendingMachine.domain.payment.strategy;

import lpanek.tdd.vendingMachine.domain.payment.*;
import lpanek.tdd.vendingMachine.domain.payment.strategy.ex.UnableToDetermineChangeException;

/**
 * Change determining strategy that picks coins for change from the highest to the lowest accessible denomination. <br/>
 * <br/>
 * <b>Example:</b><br/>
 * Let's assume that:
 * <ul>
 * <li>accessible coins are 8 x 0.10 PLN, 4 x 0.20 PLN, and 1 x 0.50 PLN,</li>
 * <li>change value is 0.8 PLN.</li>
 * </ul>
 * This strategy then returns 1 x 0.50 PLN, 1 x 0.20 PLN, and 1 x 0.10 PLN.
 */
public class HighestDenominationFirstStrategy implements ChangeDeterminingStrategy {

    @Override
    public Coins determineChange(Coins accessibleCoins, Money changeValue) throws UnableToDetermineChangeException {
        Coins change = new Coins();
        Money remainingValue = changeValue;

        for (Coin coin : Coin.valuesFromHighestToLowest()) {
            while (remainingValue.isGreaterOrEqualTo(coin.getValue()) && (accessibleCoins.getCoinCount(coin) > 0)) {
                change = change.plus(coin);
                accessibleCoins = accessibleCoins.minus(coin);
                remainingValue = remainingValue.minus(coin.getValue());
            }
        }

        if (remainingValue.equals(Money.ZERO)) {
            return change;
        }

        throw new UnableToDetermineChangeException();
    }
}
