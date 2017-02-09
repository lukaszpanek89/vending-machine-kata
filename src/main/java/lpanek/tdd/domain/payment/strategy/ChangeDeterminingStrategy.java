package lpanek.tdd.domain.payment.strategy;

import lpanek.tdd.domain.payment.Coins;
import lpanek.tdd.domain.payment.Money;

public interface ChangeDeterminingStrategy {

    Coins determineChange(Coins accessibleCoins, Money changeValue);
}
