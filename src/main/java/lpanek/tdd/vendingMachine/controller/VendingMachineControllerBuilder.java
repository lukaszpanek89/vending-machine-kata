package lpanek.tdd.vendingMachine.controller;

import lpanek.tdd.domain.payment.Coins;
import lpanek.tdd.domain.shelves.Shelves;
import lpanek.tdd.vendingMachine.physicalParts.*;

public class VendingMachineControllerBuilder {

    private Display display = new Display();
    private Keyboard keyboard = new Keyboard();
    private CoinTaker coinTaker = new CoinTaker();
    private ProductDispenser productDispenser = new ProductDispenser();
    private Shelves shelves = new Shelves();
    private Coins coins = new Coins();

    public VendingMachineControllerBuilder withDisplay(Display display) {
        this.display = display;
        return this;
    }

    public VendingMachineControllerBuilder withKeyboard(Keyboard keyboard) {
        this.keyboard = keyboard;
        return this;
    }

    public VendingMachineControllerBuilder withCoinTaker(CoinTaker coinTaker) {
        this.coinTaker = coinTaker;
        return this;
    }

    public VendingMachineControllerBuilder withProductDispenser(ProductDispenser productDispenser) {
        this.productDispenser = productDispenser;
        return this;
    }

    public VendingMachineControllerBuilder withShelves(Shelves shelves) {
        this.shelves = shelves;
        return this;
    }

    public VendingMachineControllerBuilder withCoins(Coins coins) {
        this.coins = coins;
        return this;
    }

    public VendingMachineController build() {
        return new VendingMachineController(display, keyboard, coinTaker, productDispenser, shelves, coins);
    }
}
