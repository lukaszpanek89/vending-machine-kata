package lpanek.tdd.domain.payment.strategy;

import java.util.*;

import lpanek.tdd.domain.payment.*;
import lpanek.tdd.domain.payment.strategy.ex.UnableToDetermineChangeException;

/**
 * Change determining strategy that picks coins for change from the higher to the lower accessible denomination. <br/>
 * <br/>
 * <b>Example:</b><br/>
 * Let's assume that:
 * <ul>
 * <li>accessible coins are 8 x 0.10 zł, 4 x 0.20 zł, and 1 x 0.50 zł,</li>
 * <li>change value is 0.8 zł.</li>
 * </ul>
 * This strategy then returns 1 x 0.50 zł, 1 x 0.20 zł, and 1 x 0.10 zł.
 */
public class HigherDenominationFirstStrategy implements ChangeDeterminingStrategy {

    private static final Coin[] COINS_FROM_HIGHER_TO_LOWER = getCoinsFromHigherToLower();

    @Override
    public Coins determineChange(Coins accessibleCoins, Money changeValue) throws UnableToDetermineChangeException {
        Coins change = new Coins();
        Money remainingValue = changeValue;

        for (Coin coin : COINS_FROM_HIGHER_TO_LOWER) {
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

    private static Coin[] getCoinsFromHigherToLower() {
        List<Coin> coins = Arrays.asList(Coin.values());
        Collections.reverse(coins);
        return (Coin[]) coins.toArray();
    }
}
