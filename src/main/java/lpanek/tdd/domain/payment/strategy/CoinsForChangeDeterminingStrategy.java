package lpanek.tdd.domain.payment.strategy;

import lpanek.tdd.domain.payment.Coins;
import lpanek.tdd.domain.payment.Money;

public interface CoinsForChangeDeterminingStrategy {

    Coins determineCoinsForChange(Coins accessibleCoins, Money changeValue);
}
