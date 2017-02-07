package lpanek.tdd.vendingMachine.physicalParts;

import lpanek.tdd.domain.payment.Coin;
import lpanek.tdd.vendingMachine.physicalParts.listeners.CoinTakerListener;

public class CoinTaker {

    public void insert(Coin coin) {
        // Communication with coin processing driver should happen here.
    }

    public void addListener(CoinTakerListener listener) {

    }
}
