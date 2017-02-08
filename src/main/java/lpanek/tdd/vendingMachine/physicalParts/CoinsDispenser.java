package lpanek.tdd.vendingMachine.physicalParts;

import java.util.LinkedList;
import java.util.List;

import lpanek.tdd.domain.payment.Coins;
import lpanek.tdd.vendingMachine.physicalParts.ex.NoCoinsToTakeException;
import lpanek.tdd.vendingMachine.physicalParts.ex.PreviousCoinsNotYetTakenException;
import lpanek.tdd.vendingMachine.physicalParts.listeners.CoinsDispenserListener;

public class CoinsDispenser {

    private Coins dispensedCoins;
    private List<CoinsDispenserListener> listeners = new LinkedList<>();

    public void dispenseCoins(Coins coins) throws PreviousCoinsNotYetTakenException {
        // Communication with coins dispensing driver should happen here.

        dispensedCoins = coins;

        listeners.stream().forEach(CoinsDispenserListener::onCoinsDispensed);
    }

    public Coins takeCoins() throws NoCoinsToTakeException {
        listeners.stream().forEach(CoinsDispenserListener::onCoinsTaken);

        Coins coinsToReturn = dispensedCoins;
        dispensedCoins = null;
        return coinsToReturn;
    }

    public void addListener(CoinsDispenserListener listener) {
        listeners.add(listener);
    }

    // TODO: This method is for testing purposes only. Should not be here.
    public void setDispensedCoins(Coins dispensedCoins) {
        this.dispensedCoins = dispensedCoins;
    }
}
