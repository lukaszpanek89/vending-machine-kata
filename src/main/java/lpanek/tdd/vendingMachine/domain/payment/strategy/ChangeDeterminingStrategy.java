package lpanek.tdd.vendingMachine.domain.payment.strategy;

import lpanek.tdd.vendingMachine.domain.payment.Coins;
import lpanek.tdd.vendingMachine.domain.payment.Money;
import lpanek.tdd.vendingMachine.domain.payment.strategy.ex.UnableToDetermineChangeException;

public interface ChangeDeterminingStrategy {

    Coins determineChange(Coins accessibleCoins, Money changeValue) throws UnableToDetermineChangeException;
}
