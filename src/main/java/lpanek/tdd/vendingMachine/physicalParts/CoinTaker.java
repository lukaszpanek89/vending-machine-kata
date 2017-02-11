package lpanek.tdd.vendingMachine.physicalParts;

import java.util.LinkedList;
import java.util.List;

import lpanek.tdd.vendingMachine.domain.payment.Coin;
import lpanek.tdd.vendingMachine.physicalParts.listeners.CoinTakerListener;

public class CoinTaker {

    private List<CoinTakerListener> listeners = new LinkedList<>();

    public void insert(Coin coin) {
        // Communication with coin processing driver should happen here.

        listeners.stream().forEach(listener -> listener.onCoinInserted(coin));
    }

    public void addListener(CoinTakerListener listener) {
        listeners.add(listener);
    }
}
