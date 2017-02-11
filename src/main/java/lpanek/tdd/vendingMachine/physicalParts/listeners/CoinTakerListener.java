package lpanek.tdd.vendingMachine.physicalParts.listeners;

import lpanek.tdd.vendingMachine.domain.payment.Coin;

public interface CoinTakerListener {

    void onCoinInserted(Coin coin);
}
