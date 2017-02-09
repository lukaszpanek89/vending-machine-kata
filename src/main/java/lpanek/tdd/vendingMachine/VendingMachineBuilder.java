package lpanek.tdd.vendingMachine;

import lpanek.tdd.domain.payment.Coins;
import lpanek.tdd.domain.payment.strategy.CoinsForChangeDeterminingStrategy;
import lpanek.tdd.domain.shelves.Shelves;
import lpanek.tdd.vendingMachine.controller.VendingMachineController;
import lpanek.tdd.vendingMachine.physicalParts.*;

public class VendingMachineBuilder {

    private Shelves shelves = new Shelves();
    private Coins totalCoins = new Coins();
    private CoinsForChangeDeterminingStrategy changeStrategy = (accessibleCoins, changeValue) -> null;

    public VendingMachineBuilder with(Shelves shelves) {
        this.shelves = shelves;
        return this;
    }

    public VendingMachineBuilder with(Coins totalCoins) {
        this.totalCoins = totalCoins;
        return this;
    }

    public VendingMachineBuilder with(CoinsForChangeDeterminingStrategy changeStrategy) {
        this.changeStrategy = changeStrategy;
        return this;
    }

    public VendingMachine build() {
        GlassCase glassCase = new GlassCase(shelves);
        Display display = new Display();
        Keyboard keyboard = new Keyboard();
        CoinTaker coinTaker = new CoinTaker();
        CoinsDispenser coinsDispenser = new CoinsDispenser();
        ProductDispenser productDispenser = new ProductDispenser(shelves);

        new VendingMachineController(display, keyboard, coinTaker, coinsDispenser, productDispenser, shelves, totalCoins, changeStrategy);

        return new VendingMachine(glassCase, display, keyboard, coinTaker, productDispenser);
    }
}
