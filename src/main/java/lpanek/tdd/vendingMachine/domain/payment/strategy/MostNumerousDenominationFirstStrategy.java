package lpanek.tdd.vendingMachine.domain.payment.strategy;

import lpanek.tdd.vendingMachine.domain.payment.Coins;
import lpanek.tdd.vendingMachine.domain.payment.Money;
import lpanek.tdd.vendingMachine.domain.payment.strategy.ex.UnableToDetermineChangeException;

/**
 * Change determining strategy that picks coins for change from the most numerous to the least numerous accessible denomination (if two
 * denominations are equinumerous, it picks a higher one).<br/>
 * This strategy can be helpful if one has to take into account such criteria as limited capacity of money container.<br/>
 * <br/>
 * <b>Example:</b><br/>
 * Let's assume that:
 * <ul>
 * <li>accessible coins are 8 x 0.10 PLN, 4 x 0.20 PLN, and 1 x 0.50 PLN,</li>
 * <li>change value is 0.80 PLN.</li>
 * </ul>
 * This strategy then returns 6 x 0.10 PLN, and 1 x 0.20 PLN (it first picks 4 x 0.10 PLN, then 1 x 0.20 PLN, then 1 x 0.10 PLN, and then again 1
 * x 0.10 PLN).
 */
public class MostNumerousDenominationFirstStrategy implements ChangeDeterminingStrategy {

    @Override
    public Coins determineChange(Coins accessibleCoins, Money changeValue) throws UnableToDetermineChangeException {
        // Note: This strategy is not implemented, as it was introduced just to show that different change determining strategies indeed exist. :)
        return null;
    }
}
